package voidpointer.mc.pvpaccept.data.cached;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.config.PvpConfig;
import voidpointer.mc.pvpaccept.data.DuelNotification;
import voidpointer.mc.pvpaccept.data.PvpDuelSession;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.AlreadyDuelingException;
import voidpointer.mc.pvpaccept.exception.PlayerNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpAlreadyRequestedException;
import voidpointer.mc.pvpaccept.exception.PvpDisableCoolDownException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.exception.PvpRequestNotFoundException;
import voidpointer.mc.pvpaccept.exception.RequestSenderOfflineException;
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
    private final DuelNotification duelNotification;

    public InMemoryPvpService(final PvpConfig pvpConfig, final PluginScheduler pluginScheduler, final DuelNotification duelNotification) {
        this.pvpConfig = pvpConfig;
        this.pluginScheduler = pluginScheduler;
        this.duelNotification = duelNotification;
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
        Player requestReceiver = findOnlineByName(requestReceiverName);
        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestReceiver.getUniqueId());
        if (requests.contains(requestSender))
            throw new PvpAlreadyRequestedException(requestReceiver);
        requests.add(requestSender);
        return requestReceiver;
    }

    @Override public @NotNull PvpDuelSession acceptPvpRequest(@NotNull final Player requestedPlayer, @NotNull final String requestSenderName) {
        Player requestSender = findOnlineByName(requestSenderName);
        assertNotDueling(requestedPlayer, requestSender);

        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (!requests.remove(requestSender.getUniqueId()))
            throw new PvpRequestNotFoundException();

        var duel = new PvpDuelSession(requestedPlayer, requestSender, new Date(currentTimeMillis() + pvpConfig.getPvpFinishesIn()));
        pvpRepository.addPvpDuelSession(duel);
        scheduleDraw(duel);

        return duel;
    }

    @Override public @NotNull PvpDuelSession acceptLast(@NotNull final Player requestedPlayer) {
        assertNotDueling(requestedPlayer);

        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (requests.isEmpty())
            throw new PvpRequestNotFoundException();
        UUID requestSenderUniqueId = requests.removeLast();
        Player requestSender = Bukkit.getPlayer(requestSenderUniqueId);

        PvpDuelSession duel;
        if (requestSender != null && requestSender.isOnline())
            duel = new PvpDuelSession(requestedPlayer, requestSender, new Date(currentTimeMillis() + pvpConfig.getPvpFinishesIn()));
        else
            throw new RequestSenderOfflineException();

        pvpRepository.addPvpDuelSession(duel);
        scheduleDraw(duel);

        return duel;
    }

    private void scheduleDraw(final PvpDuelSession duel) {
        pluginScheduler.async(() -> {
            if (pvpRepository.removePvpDuelSession(duel))
                duelNotification.notifyDraw(duel);
        }, pvpConfig.getPvpFinishesIn());
    }

    @Override public @NotNull Player denyFromPlayer(@NotNull final Player requestedPlayer, @NotNull final String requestSenderName) {
        Player requestSender = findOnlineByName(requestSenderName);

        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (!requests.remove(requestSender.getUniqueId()))
            throw new PvpRequestNotFoundException();

        return requestSender;
    }

    @Override public @NotNull Player denyLast(@NotNull final Player requestedPlayer) {
        Deque<UUID> requests = pvpRepository.getPvpRequestsFor(requestedPlayer.getUniqueId());
        if (requests.isEmpty())
            throw new PvpRequestNotFoundException();

        final Player requestSender = Bukkit.getPlayer(requests.removeLast());
        if (requestSender == null || !requestedPlayer.isOnline())
            throw new RequestSenderOfflineException();
        return requestSender;
    }

    @Override public Optional<PvpDuelSession> duelOf(final @NotNull Player player) {
        for (final PvpDuelSession duel : pvpRepository.getPvpDuelSessions())
            if (duel.isCombatant(player))
                return Optional.of(duel);
        return Optional.empty();
    }

    @Override public void nominateDuelWinner(@NotNull final Player killer, @NotNull final PvpDuelSession duel) {
        if (!pvpRepository.removePvpDuelSession(duel)) {
            log.debug("Tried to nominate a duel winner when the duel already finished. {}", duel);
            return; /* duel already finished */
        }
        duelNotification.notifyVictory(killer.getUniqueId(), duel);
    }

    public void clear() {
        pvpRepository.clear();
    }

    private @NotNull Player findOnlineByName(@NotNull final String playerName) throws PlayerNotFoundException {
        for (final Player onlinePlayer : Bukkit.getOnlinePlayers())
            if (onlinePlayer.getName().equalsIgnoreCase(playerName))
                return onlinePlayer;
        throw new PlayerNotFoundException(playerName);
    }

    private void assertNotDueling(final Player requestedPlayer, final Player requestSender) {
        for (final PvpDuelSession duel : pvpRepository.getPvpDuelSessions()) {
            if (duel.isCombatant(requestedPlayer))
                throw new AlreadyDuelingException(requestedPlayer, requestSender);
            else if (duel.isCombatant(requestedPlayer))
                throw new AlreadyDuelingException(requestedPlayer);
        }
    }

    private void assertNotDueling(final Player requestedPlayer) {
        for (final PvpDuelSession pvpDuelSession : pvpRepository.getPvpDuelSessions()) {
            if (pvpDuelSession.isCombatant(requestedPlayer))
                throw new AlreadyDuelingException(requestedPlayer);
        }
    }
}
