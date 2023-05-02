package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.PluginCommandNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

public final class PvpAcceptCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) throws PvpException {
        final PluginCommand pvpCommand = plugin.getCommand("pvp-send");
        if (pvpCommand == null)
            throw new PluginCommandNotFoundException();
        pvpCommand.setExecutor(new PvpAcceptCommand(locale, pvpService));
    }

    private PvpAcceptCommand(final Locale locale, final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override protected void execute(final Args args) {
        args.pollFirstIfPresentOrElse(target -> pvpService.acceptPvpRequest(args.assertPlayer(), target),
                () -> pvpService.acceptLast(args.assertPlayer()));
        // TODO send success message
    }
}
