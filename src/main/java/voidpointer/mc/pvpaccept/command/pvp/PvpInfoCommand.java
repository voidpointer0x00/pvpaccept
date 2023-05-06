package voidpointer.mc.pvpaccept.command.pvp;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpDuelSession;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.DuelNotFoundException;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpInfoMessage;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.parsed;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;
import static voidpointer.mc.pvpaccept.locale.FormatMessage.DATE_FORMAT;

public class PvpInfoCommand extends AbstractPvpCommand {

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpService pvpService) {
        final PluginCommand pvpCommand = plugin.getCommand("pvp-info");
        if (pvpCommand == null) {
            plugin.getSLF4JLogger().error("Unable to register /pvp-info command, because it's missing in description file.");
            return;
        }
        pvpCommand.setExecutor(new PvpInfoCommand(locale, pvpService));
    }

    private PvpInfoCommand(@NotNull final Locale locale, @NotNull final PvpService pvpService) {
        super(locale, pvpService);
    }

    @Override protected void execute(final Args args) throws PvpException {
        PvpDuelSession duel = pvpService.duelOf(args.assertPlayer()).orElseThrow(DuelNotFoundException::new);
        locale.send(args.audience(), PvpInfoMessage.PVP_INFO,
                parsed("formatted_duration", formatDuration(duel.timeLeft(), locale.raw(DATE_FORMAT), false)));
    }
}
