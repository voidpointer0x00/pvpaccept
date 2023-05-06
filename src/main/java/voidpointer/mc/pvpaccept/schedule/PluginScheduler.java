package voidpointer.mc.pvpaccept.schedule;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public final class PluginScheduler {
    private final Plugin plugin;

    public void async(final ThrowingRunnable task, final long delayInTicks) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {
                task.run();
            } catch (final Exception exception) {
                plugin.getSLF4JLogger().warn("Exception while executing async plugin task: {}", exception.getMessage());
                plugin.getSLF4JLogger().debug("", exception);
            }
        }, delayInTicks);
    }

    @FunctionalInterface public interface ThrowingRunnable { void run() throws Exception; }
}
