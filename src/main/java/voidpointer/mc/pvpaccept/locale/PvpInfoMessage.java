package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum PvpInfoMessage implements LocaleMessage {
    PVP_DRAW("<dark_gray> <b>»</b> " +
            "<yellow>PvP дуль между <gold><u><requested></u></gold> и <gold><u><request_sender></u></gold> завершилась ничьёй!"),
    PVP_VICTORY("<dark_gray> <b>»</b> <yellow><green><u><winner></u></green> выйграл PvP дуэль против <gold><u><loser></u></gold>!"),
    PVP_ENABLED("<dark_gray> <b>»</b> <gold><player> <green>начал <yellow>принимать запросы на PvP."),
    PVP_DISABLED("<dark_gray> <b>»</b> <gold><player> <red>перестал <yellow>принимать запросы на PvP."),
    GOT_PVP_REQUEST("<gold><sender> <yellow>вызвал Вас на PvP дуэль.\n" +
            "<dark_gray> <b>»</b> <yellow><click:suggest_command:'/pvp-accept '>Принять <green>/pvpyes</green></click>" +
            " <click:suggest_command:'/pvp-deny '>Отклонить <red>/pvpno</red></click>"),
    SENT_PVP_REQUEST("<dark_gray> <b>»</b> <yellow>Запрос на PvP игроку <gold><player></gold> отправлен."),
    NONE_TO_DENY("<dark_gray> <b>»</b> <yellow>Нет запросов для отклонения."),
    YOUR_REQUEST_DENIED("<dark_gray> <b>»</b> <yellow>Ваш PvP запрос игроку <gold><requested></gold> отклонён."),
    YOU_DENIED_REQUEST("<dark_gray> <b>»</b> <yellow>Вы отклонили PvP запрос игрока <gold><sender></gold>."),
    DUEL_STARTED("<dark_gray> <b>»</b> <yellow>Началась PvP дуэль между <gold><sender></gold> и <gold><requested></gold>."),
    CONFIG_CHANGED("<dark_gray> <b>»</b> <yellow><i><path></i> изменено <gold><old></gold>-><gold><new></gold>."),
    PVP_INFO("<dark_gray> <b>»</b> <yellow>Оставшееся время в дуэли: <formatted_duration>");

    private final String defaultValue;

    @Override public @NotNull String path() {
        return "info." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
