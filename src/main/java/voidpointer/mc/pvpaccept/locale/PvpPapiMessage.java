package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum PvpPapiMessage implements LocaleMessage {
    ON("&atrue"), OFF("&cfalse");

    private final String defaultValue;

    @Override public @NotNull String path() {
        return "papi." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
