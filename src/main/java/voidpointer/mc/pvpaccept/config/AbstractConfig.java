package voidpointer.mc.pvpaccept.config;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

@Slf4j
public abstract class AbstractConfig {
    protected final File configFile;
    protected YamlConfiguration config;

    protected AbstractConfig(final File configFile) {
        this.configFile = configFile;
    }

    protected void loadOrSaveDefault() {
        config = configFile.exists() ? loadConfiguration(configFile) : saveDefault();
    }

    protected final YamlConfiguration saveDefault() {
        YamlConfiguration config = new YamlConfiguration();
        applyDefaults(config);
        try {
            config.save(configFile);
        } catch (final IOException ioException) {
            log.warn("Unable to save configuration file {}: {}", configFile.getName(), ioException.getMessage());
        }
        return config;
    }

    protected void applyDefaults(final YamlConfiguration config) {

    }
}
