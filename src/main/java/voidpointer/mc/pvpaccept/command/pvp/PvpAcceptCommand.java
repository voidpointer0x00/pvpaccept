package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpDuelSession;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.PluginCommandNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

import static net.kyori.adventure.audience.Audience.audience;
import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component;
import static voidpointer.mc.pvpaccept.locale.PvpInfoMessage.DUEL_STARTED;

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
        final Player requested = args.assertPlayer();
        args.pollFirstIfPresentOrElse(target -> notifyDuelStarted(pvpService.acceptPvpRequest(requested, target)),
                () -> notifyDuelStarted(pvpService.acceptLast(requested)));
    }

    private void notifyDuelStarted(final PvpDuelSession duel) {
        locale.send(audience(Bukkit.getOnlinePlayers()), DUEL_STARTED, component("requested", duel.requestedName()),
                component("sender",duel.senderName()));
    }
}
