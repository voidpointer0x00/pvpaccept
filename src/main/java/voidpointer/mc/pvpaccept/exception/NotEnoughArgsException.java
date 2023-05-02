package voidpointer.mc.pvpaccept.exception;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;

import java.util.List;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.unparsed;

public class NotEnoughArgsException extends PvpException {
    private final String alias;
    private final List<String> missingArgs;

    public NotEnoughArgsException(final String alias, final String... missingArgs) {
        super(PvpErrorMessage.NOT_ENOUGH_ARGS);
        this.alias = alias;
        this.missingArgs = List.of(missingArgs);
    }

    @Override protected TagResolver[] tagResolvers(final Locale locale) {
        return new TagResolver[] {unparsed("alias", alias), unparsed("missing_args", String.join(", ", missingArgs))};
    }
}
