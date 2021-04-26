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
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.gui.KeystrokesConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.aperlambda.lambdacommon.utils.LambdaUtils;

import java.util.Timer;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * Represents the client command of AuroraKeystrokes.
 */
public class KeystrokesCommand
{
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(literal("keystrokes")
                .then(literal("reload")
                        .executes(ctx -> {
                            AuroraKeystrokes.get().config.load();
                            ctx.getSource().sendFeedback(new TranslatableText("keystrokes.reloaded").formatted(Formatting.GREEN));
                            return 1;
                        }))
                .then(literal("save")
                        .executes(ctx -> {
                            AuroraKeystrokes.get().config.save();
                            ctx.getSource().sendFeedback(new TranslatableText("keystrokes.saved").formatted(Formatting.GREEN));
                            return 1;
                        }))
                .executes(ctx -> {
                    new Timer().schedule(LambdaUtils.newTimerTaskFromLambda(() ->
                            MinecraftClient.getInstance().openScreen(new KeystrokesConfigScreen(AuroraKeystrokes.get()))), 2);
                    return 1;
                }));
    }
}
