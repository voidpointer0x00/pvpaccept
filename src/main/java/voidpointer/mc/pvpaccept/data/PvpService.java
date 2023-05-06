package voidpointer.mc.pvpaccept.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.exception.PlayerNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpDisableCoolDownException;
import voidpointer.mc.pvpaccept.exception.PvpRequestNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface PvpService {
    void enableFor(@NotNull UUID playerUniqueId);

    void disableFor(@NotNull UUID playerUniqueId) throws PvpDisableCoolDownException;

    boolean isEnabledFor(@NotNull UUID playerUniqueId);

    /** @return an instance of a player who received the request. */
    @NotNull Player sendPvpRequest(@NotNull UUID requestSender, @NotNull String requestReceiver) throws PlayerNotFoundException;

    /** @return newly created duel session for the given players. */
    @NotNull PvpDuelSession acceptPvpRequest(@NotNull Player requestedPlayer, @NotNull String requestSenderName) throws PlayerNotFoundException,
            PvpRequestNotFoundException;

    /** @return newly created duel session for the given players. */
    @NotNull PvpDuelSession acceptLast(@NotNull Player requestedPlayer) throws PvpRequestNotFoundException;

    /** @return an instance of a player who sent the request if online. */
    @NotNull Player denyFromPlayer(@NotNull Player requestedPlayer, @NotNull String requestSender)
            throws PlayerNotFoundException, PvpRequestNotFoundException;

    /** @return an instance of a player who sent the last request if online. */
    @NotNull Player denyLast(@NotNull Player requestedPlayer) throws PvpRequestNotFoundException;

    Optional<PvpDuelSession> duelOf(@NotNull Player player);

    void nominateDuelWinner(@NotNull Player killer, @NotNull PvpDuelSession duel);
}
