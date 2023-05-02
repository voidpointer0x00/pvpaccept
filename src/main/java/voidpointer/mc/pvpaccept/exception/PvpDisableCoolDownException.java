package voidpointer.mc.pvpaccept.exception;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;
import static voidpointer.mc.pvpaccept.locale.FormatMessage.DATE_FORMAT;

public final class PvpDisableCoolDownException extends PvpException {
    private final long expiresIn;

    public PvpDisableCoolDownException(final long expiresIn) {
        super(PvpErrorMessage.DISABLE_COOL_DOWN);
        this.expiresIn = expiresIn;
    }

    @Override protected TagResolver[] tagResolvers(final Locale locale) {
        return new TagResolver[] {
                Placeholder.parsed("formatted_duration", formatDuration(expiresIn, locale.raw(DATE_FORMAT), false))
        };
    }
}
