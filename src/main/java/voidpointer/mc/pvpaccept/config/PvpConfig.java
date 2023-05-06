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

    public long getPvpDisableCoolDown() {
        final long pvpOnOffCoolDown = configSupplier.get().getLong("pvp.timings.on-off-cool-down");
        if (pvpOnOffCoolDown <= 0) {
            log.warn("Invalid pvp.timings.on-off-cool-down ({} <= 0), using default 5m instead.", pvpOnOffCoolDown);
            return TimeUnit.MINUTES.toSeconds(5) * 20L /* toTicks */;
        }
        return pvpOnOffCoolDown;
    }

    public long getPvpFinishesIn() {
        final long pvpFinishesIn = configSupplier.get().getLong("pvp.timings.pvp-force-finish");
        if (pvpFinishesIn <= 0) {
            log.warn("Invalid pvp.timings.pvp-force-finish ({} <= 0), using default 5m instead.", pvpFinishesIn);
            return TimeUnit.MINUTES.toSeconds(5) * 20L /* to ticks */;
        }
        return pvpFinishesIn;
    }

    public boolean getDefaultPvpStatus() {
        return configSupplier.get().getBoolean("pvp.default-pvp-status", false);
    }
}
