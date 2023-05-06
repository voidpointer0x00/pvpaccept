package voidpointer.mc.pvpaccept.papi;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpPapiMessage;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public final class PvpPlaceholderExpansion extends PlaceholderExpansion {

    @SuppressWarnings("UnstableApiUsage")
    public static void registerExpansion(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        new PvpPlaceholderExpansion(
                plugin.getSLF4JLogger(),
                () -> plugin.getPluginMeta().getAuthors().get(0),
                plugin.getPluginMeta()::getVersion,
                locale,
                pvpService
        ).register();
    }

    private final Logger log;
    private final Supplier<String> authorSupplier;
    private final Supplier<String> versionSupplier;

    private final Map<String, Function<OfflinePlayer, String>> placeholderResolvers = ImmutableMap.of("status", this::resolveStatus);
    private final Locale locale;
    private final PvpService pvpService;

    @Override public @NotNull String getIdentifier() {
        return "pvp";
    }

    @Override public @NotNull String getAuthor() {
        return authorSupplier.get();
    }

    @Override public @NotNull String getVersion() {
        return versionSupplier.get();
    }

    @Override public boolean persist() {
        return true;
    }

    @Override public boolean register() {
        boolean isRegistered = super.register();
        log.info("PAPI hooked: {}", isRegistered);
        return isRegistered;
    }

    @Override public @Nullable String onRequest(final OfflinePlayer player, @NotNull final String params) {
        if (player == null)
            return null;
        if (!placeholderResolvers.containsKey(params.toLowerCase())) {
            log.warn("Unknown PAPI placeholder: {}_{}", getIdentifier(), params); // TODO test how displayed
            return null;
        }
        return placeholderResolvers.get(params.toLowerCase()).apply(player);
    }

    private @Nullable String resolveStatus(final OfflinePlayer player) {
        return locale.raw(pvpService.isEnabledFor(player.getUniqueId()) ? PvpPapiMessage.ON : PvpPapiMessage.OFF);
    }
}
