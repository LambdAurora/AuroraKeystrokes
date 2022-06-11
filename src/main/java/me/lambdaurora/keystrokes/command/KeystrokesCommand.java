/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes.command;

import com.mojang.brigadier.CommandDispatcher;
import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.gui.KeystrokesConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.quiltmc.qsl.command.api.client.QuiltClientCommandSource;

import java.util.Timer;
import java.util.TimerTask;

import static org.quiltmc.qsl.command.api.client.ClientCommandManager.literal;

/**
 * Represents the client command of AuroraKeystrokes.
 */
public class KeystrokesCommand {
    public static void registerCommands(CommandDispatcher<QuiltClientCommandSource> dispatcher) {
        dispatcher.register(literal("keystrokes")
                .then(literal("reload")
                        .executes(ctx -> {
                            AuroraKeystrokes.get().config.load();
                            ctx.getSource().sendFeedback(Text.translatable("keystrokes.reloaded").formatted(Formatting.GREEN));
                            return 1;
                        }))
                .then(literal("save")
                        .executes(ctx -> {
                            AuroraKeystrokes.get().config.save();
                            ctx.getSource().sendFeedback(Text.translatable("keystrokes.saved").formatted(Formatting.GREEN));
                            return 1;
                        }))
                .executes(ctx -> {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            var client = MinecraftClient.getInstance();
                            client.execute(() -> client.setScreen(new KeystrokesConfigScreen(AuroraKeystrokes.get())));
                        }
                    }, 2);
                    return 1;
                }));
    }
}
