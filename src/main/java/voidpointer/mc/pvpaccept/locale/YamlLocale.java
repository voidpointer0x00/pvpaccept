package voidpointer.mc.pvpaccept.locale;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import voidpointer.mc.pvpaccept.config.AbstractConfig;

import java.io.File;

public final class YamlLocale extends AbstractConfig implements Locale {
    private final Logger log;

    public YamlLocale(final Logger log, final File dataFolder) {
        super(new File(dataFolder, "locale.yml"));
        this.log = log;
        loadOrSaveDefault();
    }

    @Override public void send(@NotNull final Audience audience, @NotNull final LocaleMessage localeMessage,
                               final TagResolver... resolvers) {
        audience.sendMessage(MiniMessage.miniMessage().deserialize(raw(localeMessage), resolvers));
    }

    @Override public @NotNull String raw(@NotNull final LocaleMessage localeMessage) {
        if (!config.contains(localeMessage.path()))
            log.warn("[{}] Missing locale entry {}", configFile.getName(), localeMessage.path());
        return config.getString(localeMessage.path(), localeMessage.defaultValue());
    }

    @Override protected void applyDefaults(final YamlConfiguration config) {
        // TODO implement
    }
}
