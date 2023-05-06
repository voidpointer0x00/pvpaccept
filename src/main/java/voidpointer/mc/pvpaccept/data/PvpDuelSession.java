package voidpointer.mc.pvpaccept.data;

import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded=true)
public final class PvpDuelSession {
    @EqualsAndHashCode.Include
    private final UUID requested;
    private final ComponentLike requestedName;
    @EqualsAndHashCode.Include
    private final UUID requestSender;
    private final ComponentLike senderName;
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
