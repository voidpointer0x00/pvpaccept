package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum PvpInfoMessage implements LocaleMessage {
    PVP_DRAW("<dark_gray><b> » " +
            "<yellow>PvP дуль между <gold><u><requested></u></gold> и <gold><u><request_sender></u></gold> завершилась ничьёй!"),
    PVP_VICTORY("<dark_gray><b> » <yellow><green><u><winner></u></green> выйграл PvP дуель против <gold><u><loser></u></gold>!"),
    PVP_ENABLED("<dark_gray><b> » <gold><player> <green>начал <yellow>принимать запросы на PvP."),
    PVP_DISABLED("<dark_gray><b> » <gold><player> <red>перестал <yellow>принимать запросы на PvP."),
    GOT_PVP_REQUEST("<gold><sender> <yellow>вызвал Вас на PvP дуэль.\n" +
            "<dark_gray><b> » <green><click:run_command:'/pvp-accept <sender>'>Принять /pvpyes</click>" +
            " <red><click:run_command:'/pvp-deny <sender>'>Отклонить /pvpno</click>"),
    SENT_PVP_REQUEST("<dark_gray><b> » <yellow>Запрос на PvP игроку <gold><player></gold> отправлен."),
    NONE_TO_DENY("<dark_gray><b> » <yellow>Нет запросов для отклонения."),
    YOUR_REQUEST_DENIED("<dark_gray><b> » <yellow>Ваш PvP запрос игроку <gold><requested></gold> отклонён."),
    YOU_DENIED_REQUEST("<dark_gray><b> » <yellow>Вы отклонили PvP запрос игрока <gold><sender></gold>."),
    DUEL_STARTED("<dark_gray><b> » <yellow>Началась PvP дуэль между <gold><sender></gold> и <gold><requested></gold>.");

    private final String defaultValue;

    @Override public @NotNull String path() {
        return "info." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
