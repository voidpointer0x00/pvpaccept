package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum InternalMessage implements LocaleMessage {
    PLUGIN_COMMAND_NOT_FOUND("Команда <cmd> не найдена в файле описания плагина paper-plugin.yml"),
    ;

    @NotNull private final String defaultValue;

    @Override public @NotNull String path() {
        return "internal." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
