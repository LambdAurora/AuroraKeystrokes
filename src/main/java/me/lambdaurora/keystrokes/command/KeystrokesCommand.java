/*
 * AuroraKeystrokes
 * Copyright (C) 2019  LambdAurora
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lambdaurora.keystrokes.command;

import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.gui.KeystrokesConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.aperlambda.lambdacommon.utils.LambdaUtils;

import java.util.Timer;

import static io.github.cottonmc.clientcommands.ArgumentBuilders.literal;

/**
 * Represents the client command of AuroraKeystrokes.
 */
public class KeystrokesCommand implements ClientCommandPlugin
{
    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher)
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
