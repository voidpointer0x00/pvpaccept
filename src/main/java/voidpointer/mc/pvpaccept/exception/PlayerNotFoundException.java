package voidpointer.mc.pvpaccept.exception;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

@Getter
public class PlayerNotFoundException extends PvpException {
    // TODO exception message modifiers
    @NotNull
    private final String playerName;

    public PlayerNotFoundException(@NotNull final String playerName) {
        super(PvpErrorMessage.PLAYER_NOT_FOUND);
        this.playerName = playerName;
    }

    @Override protected TagResolver[] tagResolvers(final Locale locale) {
        return new TagResolver[] {Placeholder.unparsed("player", playerName)};
    }
}
