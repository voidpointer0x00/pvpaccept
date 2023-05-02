package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.NotEnoughArgsException;
import voidpointer.mc.pvpaccept.exception.PluginCommandNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

public class PvpSendCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        final PluginCommand pvpCommand = plugin.getCommand("pvp-send");
        if (pvpCommand == null)
            throw new PluginCommandNotFoundException();
        pvpCommand.setExecutor(new PvpSendCommand(locale, pvpService));
    }

    private PvpSendCommand(final Locale locale, final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override protected void execute(final Args args) throws PvpException {
        args.pollFirstIfPresentOrThrow(
                (receiverName) -> pvpService.sendPvpRequest(args.assertPlayer().getUniqueId(), receiverName),
                () -> new NotEnoughArgsException("pvp-send", "<player>"));
        // TODO send success message
    }
}
