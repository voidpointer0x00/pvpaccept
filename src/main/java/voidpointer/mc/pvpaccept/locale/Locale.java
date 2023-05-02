package voidpointer.mc.pvpaccept.locale;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public interface Locale {
    void send(@NotNull Audience audience, @NotNull LocaleMessage localeMessage, @NotNull TagResolver... resolvers);

    @NotNull String raw(@NotNull LocaleMessage localeMessage);
}
