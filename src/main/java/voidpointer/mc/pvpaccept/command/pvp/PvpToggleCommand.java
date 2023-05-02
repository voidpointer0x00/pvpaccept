package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.PluginCommandNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

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

    @Override
    protected void execute(final Args args) throws PvpException {

    }

    private void execute(final Player player) throws PvpException {
        if (pvpService.isEnabledFor(player.getUniqueId()))
            pvpService.disableFor(player.getUniqueId());
        else
            pvpService.enableFor(player.getUniqueId());
        // TODO send success message
    }
}
