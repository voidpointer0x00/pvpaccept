package voidpointer.mc.pvpaccept.data.cached;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.config.PvpConfig;
import voidpointer.mc.pvpaccept.data.DuelNotificationFactory;
import voidpointer.mc.pvpaccept.data.PvpDuelSession;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.AlreadyDuelingException;
import voidpointer.mc.pvpaccept.exception.PlayerNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpAlreadyRequestedException;
import voidpointer.mc.pvpaccept.exception.PvpDisableCoolDownException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.exception.PvpRequestNotFoundException;
import voidpointer.mc.pvpaccept.schedule.PluginScheduler;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Date;
import java.util.Deque;
import java.util.Optional;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

@Slf4j
@ThreadSafe
public final class InMemoryPvpService implements PvpService {
    private final PvpConfig pvpConfig;
    private final PluginScheduler pluginScheduler;
    private final CachedPvpRepository pvpRepository;
    private final DuelNotificationFactory duelNotificationFactory;

    public InMemoryPvpService(final PvpConfig pvpConfig, final PluginScheduler pluginScheduler, final DuelNotificationFactory duelNotificationFactory) {
        this.pvpConfig = pvpConfig;
        this.pluginScheduler = pluginScheduler;
        this.duelNotificationFactory = duelNotificationFactory;
        this.pvpRepository = new CachedPvpRepository();
    }

    @Override public void enableFor(@NotNull final UUID playerUniqueId) {
        pvpRepository.setPvp(playerUniqueId, true);
        pvpRepository.setPvpDisableCoolDown(playerUniqueId, pvpConfig.getPvpDisableCoolDown());
        pluginScheduler.async(() -> pvpRepository.resetPvpDisableCoolDown(playerUniqueId), pvpConfig.getPvpDisableCoolDown());
    }

    @Override public void disableFor(@NotNull final UUID playerUniqueId) throws PvpException {
        pvpRepository.getPvpDisableCoolDownExpiresAt(playerUniqueId).ifPresent(cdExpiresAt -> {
            final Date now = new Date();
            if (now.before(cdExpiresAt)) /* not yet expired */
                throw new PvpDisableCoolDownException(cdExpiresAt.getTime() - now.getTime());
            pvpRepository.resetPvpDisableCoolDown(playerUniqueId);
        });
        pvpRepository.setPvp(playerUniqueId, false);
    }

    @Override public boolean isEnabledFor(@NotNull final UUID playerUniqueId) {
        return pvpRepository.getPvp(playerUniqueId).orElseGet(pvpConfig::getDefaultPvpStatus);
    }

    @Override public @NotNull Player sendPvpRequest(@NotNull final UUID requestSender, @NotNull final String requestReceiverName) {
        Player requestReceiver = findByName(requestReceiverName);
        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestReceiver.getUniqueId());
        if (requests.contains(requestSender))
            throw new PvpAlreadyRequestedException(requestReceiver);
        requests.add(requestSender);
        return requestReceiver;
    }

    @Override public void acceptPvpRequest(@NotNull final Player requestedPlayer, @NotNull final String requestSenderName) {
        Player requestSender = findByName(requestSenderName);
        assertNotDueling(requestedPlayer, requestSender);

        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (!requests.remove(requestSender.getUniqueId()))
            throw new PvpRequestNotFoundException();

        var duel = new PvpDuelSession(requestedPlayer, requestSender, new Date(currentTimeMillis() + pvpConfig.getPvpFinishesIn()));
        pvpRepository.addPvpDuelSession(duel);
        // TODO schedule duel finish task & destroy if any combatant dies
        scheduleDraw(duel);
    }

    @Override public void acceptLast(@NotNull final Player requestedPlayer) {
        assertNotDueling(requestedPlayer);

        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (requests.isEmpty())
            throw new PvpRequestNotFoundException();
        UUID requestSenderUniqueId = requests.removeLast();
        Player requestSender = Bukkit.getPlayer(requestSenderUniqueId);

        PvpDuelSession duel;
        if (requestSender != null) {
            duel = new PvpDuelSession(requestedPlayer, requestSender, new Date(currentTimeMillis() + pvpConfig.getPvpFinishesIn()));
        } else {
            log.warn("Unable to fetch Player instance for {}, running offline without user cache?", requestSenderUniqueId);
            duel = new PvpDuelSession(requestedPlayer, requestSenderUniqueId, new Date(System.currentTimeMillis() + pvpConfig.getPvpFinishesIn()));
        }
        pvpRepository.addPvpDuelSession(duel);
        // TODO schedule duel finish task & destroy if any combatant dies
        scheduleDraw(duel);
    }

    private void scheduleDraw(final PvpDuelSession duel) {
        pluginScheduler.async(() -> {
            if (pvpRepository.removePvpDuelSession(duel))
                duelNotificationFactory.notifyDraw(duel);
        }, pvpConfig.getPvpFinishesIn());
    }

    @Override public @NotNull Player denyFromPlayer(@NotNull final Player requestedPlayer, @NotNull final String requestSenderName) {
        Player requestSender = findByName(requestSenderName);

        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (!requests.remove(requestSender.getUniqueId()))
            throw new PvpRequestNotFoundException();

        return requestSender;
    }

    @Override public @NotNull Optional<Player> denyLast(@NotNull final Player requestedPlayer) {
        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (requests.isEmpty())
            throw new PvpRequestNotFoundException();

        return Optional.ofNullable(Bukkit.getPlayer(requests.removeLast()));
    }

    public void clear() {
        pvpRepository.clear();
    }

    private @NotNull Player findByName(@NotNull final String playerName) throws PlayerNotFoundException {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (onlinePlayer.getName().equalsIgnoreCase(playerName))
                return onlinePlayer;
        throw new PlayerNotFoundException(playerName);
    }

    private void assertNotDueling(final Player requestedPlayer, final Player requestSender) {
        for (final PvpDuelSession pvpDuelSession : pvpRepository.getPvpDuelSessions()) {
            if (pvpDuelSession.requested().equals(requestSender.getUniqueId()))
                throw new AlreadyDuelingException(requestedPlayer, requestSender);
            else if (pvpDuelSession.requestSender().equals(requestedPlayer.getUniqueId()))
                throw new AlreadyDuelingException(requestedPlayer);
        }
    }

    private void assertNotDueling(final Player requestedPlayer) {
        for (final PvpDuelSession pvpDuelSession : pvpRepository.getPvpDuelSessions()) {
            if (pvpDuelSession.requestSender().equals(requestedPlayer.getUniqueId()))
                throw new AlreadyDuelingException(requestedPlayer);
        }
    }
}
