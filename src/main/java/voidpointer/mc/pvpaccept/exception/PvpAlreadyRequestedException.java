package voidpointer.mc.pvpaccept.exception;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

public class PvpAlreadyRequestedException extends PvpException {
    private final Player requestReceiver;

    public PvpAlreadyRequestedException(final @NotNull Player requestReceiver) {
        super(PvpErrorMessage.PVP_ALREADY_REQUESTED);
        this.requestReceiver = requestReceiver;
    }

    @Override protected TagResolver[] tagResolvers(final Locale locale) {
        return new TagResolver[] {Placeholder.component("player", requestReceiver.displayName())};
    }
}
