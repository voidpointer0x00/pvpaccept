package voidpointer.mc.pvpaccept.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public final class PvpConfig {
    private final Supplier<ConfigurationSection> configSupplier;
    private final Runnable saveConfigTask;

    public long getPvpEnableCoolDown() {
        final long pvpOnOffCoolDown = configSupplier.get().getLong("pvp.cool-downs.on-off");
        if (pvpOnOffCoolDown <= 0) {
            log.warn("Invalid pvp.cool-downs.on-off ({} <= 0), using default 5m instead.", pvpOnOffCoolDown);
            return TimeUnit.MINUTES.toMillis(5);
        }
        return pvpOnOffCoolDown;
    }

    public long getPvpFinishesIn() {
        final long pvpFinishesIn = configSupplier.get().getLong("pvp.cool-downs.pvp-finish");
        if (pvpFinishesIn <= 0) {
            log.warn("Invalid pvp.cool-downs.pvp-finish ({} <= 0), using default 5m instead.", pvpFinishesIn);
            return TimeUnit.MINUTES.toMillis(5);
        }
        return pvpFinishesIn;
    }

    public boolean getDefaultPvpStatus() {
        return configSupplier.get().getBoolean("pvp.default-pvp-status", false);
    }
}
