package voidpointer.mc.pvpaccept.command;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import voidpointer.mc.pvpaccept.exception.PlayerRequiredException;
import voidpointer.mc.pvpaccept.exception.PvpException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Args {
    @NotNull private final CommandSender sender;

    @Nullable private final LinkedList<String> args;

    public Args(@NotNull final CommandSender sender, @NotNull final String[] args) {
        this.sender = sender;
        this.args = args.length == 0 ? null : new LinkedList<>(Arrays.asList(args));
    }

    public @NotNull Player assertPlayer() throws PvpException {
        if (!(sender instanceof Player player))
            throw new PlayerRequiredException();
        return player;
    }

    public Audience audience() {
        return sender;
    }

    public void pollFirstIfPresentOrElse(final Consumer<String> argConsumer, final Runnable orElse) {
        if ((args != null) && !args.isEmpty())
            argConsumer.accept(args.pollFirst());
        else
            orElse.run();
    }

    public void pollFirstIfPresentOrThrow(final Consumer<String> argConsumer, final Supplier<PvpException> orElse) {
        if ((args != null) && !args.isEmpty())
            argConsumer.accept(args.pollFirst());
        else
            throw orElse.get();
    }
}
