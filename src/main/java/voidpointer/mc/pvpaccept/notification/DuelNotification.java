package voidpointer.mc.pvpaccept.notification;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.ComponentLike;
import voidpointer.mc.pvpaccept.data.PvpDuelSession;
import voidpointer.mc.pvpaccept.locale.Locale;

import java.util.UUID;

import static net.kyori.adventure.audience.Audience.audience;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.PVP_DRAW;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.PVP_VICTORY;

@RequiredArgsConstructor
public final class DuelNotification {
    private final Locale locale;

    public void notifyDraw(final PvpDuelSession duel) {
        locale.send(audience(getOnlinePlayers()), PVP_DRAW,
                component("requested", duel.requestedName()),
                component("request_sender", duel.senderName()));
        locale.send(getConsoleSender(), PVP_DRAW,
                component("requested", duel.requestedName()),
                component("request_sender", duel.senderName()));
    }

    public void notifyVictory(final UUID winner, final PvpDuelSession duel) {
        ComponentLike loserName, winnerName;
        if (duel.requested().equals(winner)) {
            winnerName = duel.requestedName();
            loserName = duel.senderName();
        } else {
            winnerName = duel.senderName();
            loserName = duel.requestedName();
        }
        locale.send(audience(getOnlinePlayers()), PVP_VICTORY, component("winner", winnerName), component("loser", loserName));
        locale.send(getConsoleSender(), PVP_VICTORY, component("winner", winnerName), component("loser", loserName));
    }
}
