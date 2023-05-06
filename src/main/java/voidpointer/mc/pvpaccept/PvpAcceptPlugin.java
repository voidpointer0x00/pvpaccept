package voidpointer.mc.pvpaccept;

import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.mc.pvpaccept.command.pvp.PvpAcceptCommand;
import voidpointer.mc.pvpaccept.command.pvp.PvpDenyCommand;
import voidpointer.mc.pvpaccept.command.pvp.PvpSendCommand;
import voidpointer.mc.pvpaccept.command.pvp.PvpToggleCommand;
import voidpointer.mc.pvpaccept.config.PvpConfig;
import voidpointer.mc.pvpaccept.data.DuelNotificationFactory;
import voidpointer.mc.pvpaccept.data.cached.InMemoryPvpService;
import voidpointer.mc.pvpaccept.locale.Locale;
import voidpointer.mc.pvpaccept.locale.YamlLocale;
import voidpointer.mc.pvpaccept.papi.PvpPlaceholderExpansion;
import voidpointer.mc.pvpaccept.schedule.PluginScheduler;

import java.io.File;

public final class PvpAcceptPlugin extends JavaPlugin {
    private PvpConfig pvpConfig;
    private Locale locale;
    private InMemoryPvpService pvpService;
    private DuelNotificationFactory duelNotificationFactory;

    @Override public void onLoad() {
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
        pvpConfig = new PvpConfig(this::getConfig, this::saveConfig);
        locale = new YamlLocale(getSLF4JLogger(), getDataFolder());
        duelNotificationFactory = new DuelNotificationFactory(locale);
        pvpService = new InMemoryPvpService(pvpConfig, new PluginScheduler(this), duelNotificationFactory);
    }

    @Override public void onEnable() {
        PvpPlaceholderExpansion.registerExpansion(this, locale, pvpService);
        registerCommands();
    }

    @Override public void onDisable() {
        pvpService.clear();
    }

    private void registerCommands() {
        PvpAcceptCommand.register(this, locale, pvpService);
        PvpDenyCommand.register(this, locale, pvpService);
        PvpSendCommand.register(this, locale, pvpService);
        PvpToggleCommand.register(this, locale, pvpService);
    }
}
