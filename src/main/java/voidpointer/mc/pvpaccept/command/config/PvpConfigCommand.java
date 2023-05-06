package voidpointer.mc.pvpaccept.command.config;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import voidpointer.mc.pvpaccept.config.PvpConfig;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.PvpErrorMessage;
import voidpointer.mc.pvpaccept.locale.PvpInfoMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static voidpointer.mc.pvpaccept.locale.PvpErrorMessage.INVALID_TICKS;

@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public final class PvpConfigCommand implements CommandExecutor, TabCompleter {
    private final Locale locale;
    private final PvpConfig pvpConfig;

    private Map<String, BiConsumer<Audience, Long>> commands = ImmutableMap.of("disable-cd", this::setDisableCoolDown,
            "force-finish-cd", this::setForceFinish);
    private final List<String> commandNames = List.of("disable-cd", "force-finish-cd");

    public static void register(final JavaPlugin plugin, final Locale locale, final PvpConfig pvpConfig) {
        PluginCommand pluginCommand = plugin.getCommand("pvp-config");
        if (pluginCommand == null) {
            plugin.getSLF4JLogger().error("Unable to register /pvp-config command, because it's missing in description file.");
            return;
        }
        PvpConfigCommand configCommand = new PvpConfigCommand(locale, pvpConfig);
        pluginCommand.setExecutor(configCommand);
        pluginCommand.setTabCompleter(configCommand);
    }

    @Override public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final @NotNull String[] args) {
        return switch (args.length) {
            case 0 -> commandNames;
            case 1 -> commandNames.stream().filter(cmd -> cmd.startsWith(args[0])).collect(Collectors.toList());
            default -> Collections.emptyList();
        };
    }

    @Override public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final @NotNull String[] args) {
        if (args.length < 2) {
            locale.send(sender, PvpErrorMessage.CONFIG_USAGE);
            return true;
        }
        if (!commands.containsKey(args[0])) {
            locale.send(sender, PvpErrorMessage.CONFIG_UNKNOWN, Placeholder.unparsed("given", args[0]));
            return true;
        }
        long ticks;
        try {
             ticks = Long.parseLong(args[1]);
        } catch (final NumberFormatException numberFormatException) {
            locale.send(sender, INVALID_TICKS, Placeholder.unparsed("ticks", args[1]));
            return true;
        }
        if (ticks <= 0) {
            locale.send(sender, INVALID_TICKS, Placeholder.unparsed("ticks", args[1]));
            return true;
        }
        commands.get(args[0]).accept(sender, ticks);
        return true;
    }

    private void setDisableCoolDown(final Audience audience, final long ticks) {
        locale.send(audience, PvpInfoMessage.CONFIG_CHANGED,
                Placeholder.unparsed("path", PvpConfig.PVP_DISABLE_CD_PATH),
                Placeholder.unparsed("old", Long.toString(pvpConfig.setPvpDisableCoolDownInTicks(ticks))),
                Placeholder.unparsed("new", Long.toString(ticks))
        );
    }

    private void setForceFinish(final Audience audience, final long ticks) {
        locale.send(audience, PvpInfoMessage.CONFIG_CHANGED,
                Placeholder.unparsed("path", PvpConfig.PVP_FORCE_FINISH_PATH),
                Placeholder.unparsed("old", Long.toString(pvpConfig.setPvpForceFinishInTicks(ticks))),
                Placeholder.unparsed("new", Long.toString(ticks))
        );
    }
}
