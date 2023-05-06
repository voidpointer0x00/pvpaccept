package voidpointer.mc.pvpaccept.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public final class PvpConfig {
    public static final String PVP_DISABLE_CD_PATH = "pvp.timings.on-off-cool-down";
    public static final String PVP_FORCE_FINISH_PATH = "pvp.timings.pvp-force-finish";

    private final Supplier<ConfigurationSection> configSupplier;
    private final Runnable saveConfigTask;

    public long getPvpDisableCoolDownInMillis() {
        return getPvpDisableCoolDownInTicks() * 50;
    }

    public long getPvpDisableCoolDownInTicks() {
        final long pvpOnOffCoolDown = configSupplier.get().getLong(PVP_DISABLE_CD_PATH);
        if (pvpOnOffCoolDown <= 0) {
            log.warn("Invalid pvp.timings.on-off-cool-down ({} <= 0), using default 5m instead.", pvpOnOffCoolDown);
            return TimeUnit.MINUTES.toSeconds(5) * 20L /* toTicks */;
        }
        return pvpOnOffCoolDown;
    }

    public long getPvpForceFinishInMillis() {
        return getPvpForceFinishInTicks() * 50;
    }

    public long getPvpForceFinishInTicks() {
        final long pvpFinishesIn = configSupplier.get().getLong(PVP_FORCE_FINISH_PATH);
        if (pvpFinishesIn <= 0) {
            log.warn("Invalid pvp.timings.pvp-force-finish ({} <= 0), using default 5m instead.", pvpFinishesIn);
            return TimeUnit.MINUTES.toSeconds(5) * 20L /* to ticks */;
        }
        return pvpFinishesIn;
    }

    public boolean getDefaultPvpStatus() {
        return configSupplier.get().getBoolean("pvp.default-pvp-status", false);
    }

    public long setPvpDisableCoolDownInTicks(final long ticks) {
        final long prev = getPvpDisableCoolDownInTicks();
        configSupplier.get().set(PVP_DISABLE_CD_PATH, ticks);
        saveConfigTask.run();
        return prev;
    }

    public long setPvpForceFinishInTicks(final long ticks) {
        final long prev = getPvpForceFinishInTicks();
        configSupplier.get().set(PVP_FORCE_FINISH_PATH, ticks);
        saveConfigTask.run();
        return prev;
    }
}
