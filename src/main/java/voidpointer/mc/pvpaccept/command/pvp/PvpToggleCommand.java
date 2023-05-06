package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.PluginCommandNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

import static net.kyori.adventure.audience.Audience.audience;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.PVP_DISABLED;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.PVP_ENABLED;

public final class PvpToggleCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        final PluginCommand pvpCommand = plugin.getCommand("pvp");
        if (pvpCommand == null)
            throw new PluginCommandNotFoundException();
        pvpCommand.setExecutor(new PvpToggleCommand(locale, pvpService));
    }

    private PvpToggleCommand(final Locale locale, final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override protected void execute(final Args args) throws PvpException {
        final Player player = args.assertPlayer();
        if (pvpService.isEnabledFor(player.getUniqueId())) {
            pvpService.disableFor(player.getUniqueId());
            locale.send(audience(getOnlinePlayers()), PVP_DISABLED, component("player", player.displayName()));
        } else {
            pvpService.enableFor(player.getUniqueId());
            locale.send(audience(getOnlinePlayers()), PVP_ENABLED, component("player", player.displayName()));
        }
    }
}
