package voidpointer.mc.pvpaccept.exception;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public final class AlreadyDuelingException extends PvpException {
    @NotNull private final Player requestedPlayer;
    @Nullable private final Player combatant;

    public AlreadyDuelingException(@NotNull final Player requestedPlayer) {
        super(PvpErrorMessage.CANNOT_ACCEPT_WHILE_DUELING);
        this.requestedPlayer = requestedPlayer;
        this.combatant = null;
    }

    public AlreadyDuelingException(@NotNull final Player requestedPlayer, @NotNull final Player combatant) {
        super(PvpErrorMessage.TARGET_ALREADY_DUELING);
        this.requestedPlayer = requestedPlayer;
        this.combatant = combatant;
    }

    @Override protected TagResolver[] tagResolvers(final Locale locale) {
        return new TagResolver[] {
                Placeholder.component("player", requestedPlayer.displayName()),
                Placeholder.component("combatant", combatant != null ? combatant.displayName() : Component.empty())
        };
    }
}
