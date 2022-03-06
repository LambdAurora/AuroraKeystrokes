/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes.mixin;

import me.lambdaurora.keystrokes.AuroraKeystrokes;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects the CPS counter.
 */
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "doAttack", at = @At(value = "HEAD"))
    private void aurorakeystrokes_onAttack(CallbackInfoReturnable<Boolean> cir) {
        AuroraKeystrokes.get().cps++;
    }
}
