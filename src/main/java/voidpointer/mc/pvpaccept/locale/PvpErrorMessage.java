package voidpointer.mc.pvpaccept.locale;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum PvpErrorMessage implements LocaleMessage {
    CANNOT_ACCEPT_WHILE_DUELING("<red>Сначала завершите текущую дуэль с <gold><combatant></gold>."),
    DISABLE_COOL_DOWN("<red>Вы можете отключить PvP только через <formatted_duration>."),
    NOT_ENOUGH_ARGS("<yellow>Используйте: <gold><alias> <i><u><missing_args><yellow>."),
    PLAYER_NOT_FOUND("<red>Игрок <gold>«<player>»</gold> не найден."),
    PLAYER_REQUIRED("<red>Только игрок может выполнять эту команду."),
    PVP_ALREADY_REQUESTED("<red>Вы уже вызвали <gold><player></gold> на дуэль."),
    REQUEST_NOT_FOUND("<red>Вызов на дуэль не найден."),
    TARGET_ALREADY_DUELING("<yellow><gold><player></gold> уже сражается с <gold><combatant></gold>."),
    REQUEST_SENDER_OFFLINE("<yellow>Невозможно принять дуэль: отправитель оффлайн."),
    SELF_REQUEST("<red>Нельзя отправлять заявку самому себе."),
    CONFIG_USAGE("<yellow>/pvp-config <gold>[ disable-cd | force-finish-cd ] {время в тиках} <dark_gray><i>1 сек. = 20 тиков"),
    INVALID_TICKS("<red>Вы указали неверное время в тиках <gray><i>(<ticks>)</i></gray>, должно быть целочисленное значение >= 0"),
    CONFIG_UNKNOWN("<red>Неизвестая настройка <given>, используйте одну из disable-cd, force-finish-cd."),
    DUEL_NOT_FOUND("<red>В данный момент вы не состоите в PvP дуэли.");

    private final String defaultValue;

    @Override public @NotNull String path() {
        return "error." + toString().toLowerCase().replace('_', '-');
    }

    @Override public @NotNull String defaultValue() {
        return defaultValue;
    }
}
