package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.locale.Locale;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.YOUR_REQUEST_DENIED;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.YOU_DENIED_REQUEST;

public class PvpDenyCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        final PluginCommand pvpCommand = plugin.getCommand("pvp-deny");
        if (pvpCommand == null) {
            plugin.getSLF4JLogger().error("Unable to register /pvp-deny command, because it's missing in description file.");
            return;
        }
        pvpCommand.setExecutor(new PvpDenyCommand(locale, pvpService));
    }

    private PvpDenyCommand(final Locale locale, final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override
    protected void execute(final Args args) {
        final Player requested = args.assertPlayer();
        args.pollFirstIfPresentOrElse(target -> notifyDenied(pvpService.denyFromPlayer(requested, target), requested),
                () -> notifyDenied(pvpService.denyLast(requested), requested));
    }

    private void notifyDenied(final Player sender, final Player requested) {
        locale.send(sender, YOUR_REQUEST_DENIED, component("requested", requested.displayName()));
        locale.send(requested, YOU_DENIED_REQUEST, component("sender", sender.displayName()));
    }
}
