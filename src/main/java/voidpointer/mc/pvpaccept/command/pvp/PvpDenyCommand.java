package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.PluginCommandNotFoundException;
import voidpointer.mc.pvpaccept.locale.Locale;

public class PvpDenyCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        final PluginCommand pvpCommand = plugin.getCommand("pvp-send");
        if (pvpCommand == null)
            throw new PluginCommandNotFoundException();
        pvpCommand.setExecutor(new PvpDenyCommand(locale, pvpService));
    }

    private PvpDenyCommand(final Locale locale, final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override
    protected void execute(final Args args) {
        args.pollFirstIfPresentOrElse(
                target -> pvpService.denyFromPlayer(args.assertPlayer(), target),
                () -> pvpService.denyLast(args.assertPlayer()));
        // TODO send success message
    }
}
