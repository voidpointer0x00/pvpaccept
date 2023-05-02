package voidpointer.mc.pvpaccept.command.pvp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.command.Args;
import voidpointer.mc.pvpaccept.data.PvpService;
import voidpointer.mc.pvpaccept.exception.PvpException;
import voidpointer.mc.pvpaccept.locale.Locale;

@Slf4j
@RequiredArgsConstructor
abstract class AbstractPvpCommand implements CommandExecutor {
    final Locale locale;
    final PvpService pvpService;

    @Override public final boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
                                             @NotNull final String label, final @NotNull String[] args) {
        try {
            execute(new Args(sender, args));
        } catch (final PvpException pvpException) {
            pvpException.report(locale, sender);
            log.debug(locale.raw(pvpException.getLocaleMessage()), pvpException);
        }
        return true;
    }

    protected abstract void execute(final Args args) throws PvpException;
}
