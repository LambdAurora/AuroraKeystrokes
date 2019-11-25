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

package me.lambdaurora.keystrokes.mixin;

import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.gui.KeystrokesHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
    private AuroraKeystrokes fabric_keystrokes;
    private KeystrokesHud    keystrokes_hud;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void on_new(MinecraftClient client, CallbackInfo ci)
    {
        this.fabric_keystrokes = AuroraKeystrokes.get();
        this.fabric_keystrokes.log("Adding the keystokes hud...");
        // Keystrokes hud.
        this.keystrokes_hud = new KeystrokesHud(client, this.fabric_keystrokes);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountHealth()V"))
    public void on_render(float tick_delta, CallbackInfo ci)
    {
        if (this.fabric_keystrokes.config.does_render_hud())
            keystrokes_hud.render();
    }
}
