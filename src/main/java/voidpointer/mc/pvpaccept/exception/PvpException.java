package voidpointer.mc.pvpaccept.exception;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.LocaleMessage;

public class PvpException extends RuntimeException {
    private static final TagResolver[] EMPTY_RESOLVERS = new TagResolver[0];
    @Getter private final LocaleMessage localeMessage;

    public PvpException(final LocaleMessage localeMessage) {
        this.localeMessage = localeMessage;
    }

    public PvpException(final LocaleMessage localeMessage, final String message) {
        super(message);
        this.localeMessage = localeMessage;
    }

    public void report(final Locale locale, final Audience audience) {
        locale.send(audience, localeMessage, tagResolvers(locale));
    }

    protected TagResolver[] tagResolvers(final Locale locale) {
        return EMPTY_RESOLVERS;
    }
}
