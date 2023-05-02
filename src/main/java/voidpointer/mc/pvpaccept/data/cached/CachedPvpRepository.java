package voidpointer.mc.pvpaccept.data.cached;

import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.data.PvpDuelSession;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Date;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@ThreadSafe
final class CachedPvpRepository {
    private final Map<UUID, Boolean> pvpStatusCache = new ConcurrentHashMap<>();
    private final Map<UUID, Date> pvpDisableCoolDown = new ConcurrentHashMap<>();
    private final Set<PvpDuelSession> pvpDuelSessions = ConcurrentHashMap.newKeySet();
    private final Map<UUID, Deque<UUID>> pvpRequests = new ConcurrentHashMap<>();

    public void setPvp(@NotNull final UUID playerUniqueId, final boolean pvpStatus) {
        pvpStatusCache.put(playerUniqueId, pvpStatus);
    }

    public Optional<Boolean> getPvp(@NotNull final UUID playerUniqueId) {
        return Optional.ofNullable(pvpStatusCache.get(playerUniqueId));
    }

    public void setCoolDown(@NotNull final UUID playerUniqueId, final long pvpEnableCoolDown) {
        pvpDisableCoolDown.put(playerUniqueId, new Date(System.currentTimeMillis() + pvpEnableCoolDown));
    }

    public @NotNull Optional<Date> getPvpDisableCoolDownExpiresAt(@NotNull final UUID playerUniqueId) {
        return Optional.ofNullable(pvpDisableCoolDown.get(playerUniqueId));
    }

    public void resetPvpDisableCoolDown(@NotNull final UUID playerUniqueId) {
        pvpDisableCoolDown.remove(playerUniqueId);
    }

    public Deque<UUID> getPvpRequestsFor(final UUID requestReceiver) {
        return pvpRequests.computeIfAbsent(requestReceiver, (receiver) -> new ConcurrentLinkedDeque<>());
    }

    public void addPvpDuelSession(final PvpDuelSession duel) {
        pvpDuelSessions.add(duel);
    }

    public @NotNull Set<PvpDuelSession> getPvpDuelSessions() {
        return pvpDuelSessions;
    }

    public void clear() {
        pvpStatusCache.clear();
        pvpDisableCoolDown.clear();
        pvpDuelSessions.clear();
        pvpRequests.clear();
    }
}
