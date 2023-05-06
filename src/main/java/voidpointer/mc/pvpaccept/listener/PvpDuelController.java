package voidpointer.mc.pvpaccept.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import voidpointer.mc.pvpaccept.data.PvpService;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(access=AccessLevel.PACKAGE)
public final class PvpDuelController implements Listener {

    public static void register(final Plugin plugin, final PvpService pvpService) {
        plugin.getServer().getPluginManager().registerEvents(new PvpDuelController(plugin.getSLF4JLogger(), pvpService), plugin);
    }

    private final Logger log;
    private final PvpService pvpService;

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    void handleDuelVictory(final PlayerDeathEvent deathEvent) {
        if (deathEvent.getPlayer().getKiller() == null) {
            log.debug("{}[{}] died in a non PvP situation", deathEvent.getPlayer().getName(), deathEvent.getPlayer().getUniqueId());
            return; /* not a PvP situation */
        }
        /* there's no need to run it on the main thread, so it's better to delegate the task */
        CompletableFuture.runAsync(() -> pvpService.duelOf(deathEvent.getPlayer()).ifPresent(duel -> {
            final Player killer = deathEvent.getPlayer().getKiller();
            if (killer.getUniqueId().equals(deathEvent.getPlayer().getUniqueId()))
                return; /* ignore suicide */
            if (!duel.isCombatant(killer)) /* ignore if the killer was not dueling */
                return;
            pvpService.nominateDuelWinner(killer, duel);
        }));
    }
}
