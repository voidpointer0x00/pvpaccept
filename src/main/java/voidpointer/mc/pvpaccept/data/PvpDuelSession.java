package voidpointer.mc.pvpaccept.data;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

@ToString(onlyExplicitlyIncluded=true)
@EqualsAndHashCode(onlyExplicitlyIncluded=true)
public final class PvpDuelSession {
    @EqualsAndHashCode.Include
    private final UUID requested;
    @ToString.Include
    private final ComponentLike requestedName;
    @EqualsAndHashCode.Include
    private final UUID requestSender;
    @ToString.Include
    private final ComponentLike senderName;
    @ToString.Include
    private final Date expiresAt;

    public PvpDuelSession(final Player requested, final Player requestSender, final Date expiresAt) {
        this(requested, requestSender.getUniqueId(), requestSender.displayName(), expiresAt);
    }

    public PvpDuelSession(final Player requested, final UUID requestSenderUniqueId, final Date expiresAt) {
        this(requested, requestSenderUniqueId, Component.text(requestSenderUniqueId.toString()), expiresAt);
    }

    private PvpDuelSession(final Player requested, final UUID requestSender, final ComponentLike senderName, final Date expiresAt) {
        this.requested = requested.getUniqueId();
        this.requestedName = requested.displayName();
        this.requestSender = requestSender;
        this.senderName = senderName;
        this.expiresAt = expiresAt;
    }

    public boolean isCombatant(final Player player) {
        return requested.equals(player.getUniqueId()) || requestSender.equals(player.getUniqueId());
    }

    public UUID requested() {
        return requested;
    }

    public ComponentLike requestedName() {
        return requestedName;
    }

    public UUID requestSender() {
        return requestSender;
    }

    public ComponentLike senderName() {
        return senderName;
    }

    public Date expiresAt() {
        return expiresAt;
    }
}
