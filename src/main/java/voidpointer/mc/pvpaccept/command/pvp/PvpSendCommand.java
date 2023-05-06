package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.NotEnoughArgsException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.GOT_PVP_REQUEST;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.SENT_PVP_REQUEST;

public class PvpSendCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        final PluginCommand pvpCommand = plugin.getCommand("pvp-send");
        if (pvpCommand == null) {
            plugin.getSLF4JLogger().error("Unable to register /pvp-send command, because it's missing in description file.");
            return;
        }
        pvpCommand.setExecutor(new PvpSendCommand(locale, pvpService));
    }

    private PvpSendCommand(final Locale locale, final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override protected void execute(final Args args) throws PvpException {
        args.pollFirstIfPresentOrThrow((receiverName) -> {
            final Player requested = pvpService.sendPvpRequest(args.assertPlayer().getUniqueId(), receiverName);
            locale.send(requested, GOT_PVP_REQUEST, component("sender", args.assertPlayer().displayName()));
            locale.send(args.audience(), SENT_PVP_REQUEST, component("player", requested.displayName()));
        },
        () -> new NotEnoughArgsException("pvp-send", "<player>"));
    }
}
