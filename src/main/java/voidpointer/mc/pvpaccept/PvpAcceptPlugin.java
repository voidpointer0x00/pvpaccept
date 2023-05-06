package voidpointer.mc.pvpaccept;

import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.pvp.PvpAcceptCommand;
import voidpointer.mc.pvpaccept.command.pvp.PvpDenyCommand;
import voidpointer.mc.pvpaccept.command.pvp.PvpSendCommand;
import voidpointer.mc.pvpaccept.command.pvp.PvpToggleCommand;
import voidpointer.mc.pvpaccept.config.PvpConfig;
import voidpointer.mc.pvpaccept.data.DuelNotification;
import voidpointer.mc.pvpaccept.data.cached.InMemoryPvpService;
import voidpointer.mc.pvpaccept.listener.PvpDuelController;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.YamlLocale;
import voidpointer.mc.pvpaccept.papi.PvpPlaceholderExpansion;
import voidpointer.mc.pvpaccept.schedule.PluginScheduler;

import java.io.File;

public final class PvpAcceptPlugin extends JavaPlugin {
    private Locale locale;
    private InMemoryPvpService pvpService;
    private DuelNotification duelNotification;

    @Override public void onLoad() {
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
        final PvpConfig pvpConfig = new PvpConfig(this::getConfig, this::saveConfig);

        locale = new YamlLocale(getSLF4JLogger(), getDataFolder());
        pvpService = new InMemoryPvpService(pvpConfig, new PluginScheduler(this), new DuelNotification(locale));
    }

    @Override public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null)
            getSLF4JLogger().info("Unable to hook PAPI: the plugin is missing.");
        else
            PvpPlaceholderExpansion.registerExpansion(this, locale, pvpService);
        registerCommands();
        PvpDuelController.register(this, pvpService);
    }

    @Override public void onDisable() {
        if (pvpService != null)
            pvpService.clear();
    }

    private void registerCommands() {
        PvpAcceptCommand.register(this, locale, pvpService);
        PvpDenyCommand.register(this, locale, pvpService);
        PvpSendCommand.register(this, locale, pvpService);
        PvpToggleCommand.register(this, locale, pvpService);
    }
}
