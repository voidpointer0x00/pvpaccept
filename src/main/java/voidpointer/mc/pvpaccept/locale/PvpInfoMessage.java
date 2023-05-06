package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum PvpInfoMessage implements LocaleMessage {
    PVP_DRAW("<dark_gray><b> » " +
            "<yellow>PvP дуль между <gold><u><requested></u></gold> и <gold><u><request_sender></u></gold> завершилась ничьёй!"),
    PVP_VICTORY("<dark_gray><b> » <yellow><green><u><winner></u></green> выйграл PvP дуель против <gold><u><loser></u></gold>!"),
    ;

    private final String defaultValue;

    @Override public @NotNull String path() {
        return "info." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
