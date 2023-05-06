package voidpointer.mc.pvpaccept.data;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import voidpointer.mc.pvpaccept.locale.Locale;

import java.util.UUID;

import static net.kyori.adventure.audience.Audience.audience;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.PVP_DRAW;

@RequiredArgsConstructor
public final class DuelNotification {
    private final Locale locale;

    public void notifyDraw(final PvpDuelSession duel) {
        locale.send(audience(Bukkit.getOnlinePlayers()), PVP_DRAW,
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
        locale.send(audience(Bukkit.getOnlinePlayers()), PVP_DRAW, component("winner", winnerName), component("loser", loserName));
    }
}
