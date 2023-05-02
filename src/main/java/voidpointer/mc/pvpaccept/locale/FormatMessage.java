package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum FormatMessage implements LocaleMessage {
    DATE_FORMAT("<gold>mmмин., ssсек.</gold>"),
    ;

    @NotNull private final String defaultValue;

    @Override public @NotNull String path() {
        return "format." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
