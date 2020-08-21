/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes.gui;

import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.LayoutMode;
import me.lambdaurora.spruceui.hud.Hud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the keystrokes hud.
 */
public class KeystrokesHud extends Hud
{
    private final AuroraKeystrokes mod;
    private       MinecraftClient  client;

    public KeystrokesHud(AuroraKeystrokes mod)
    {
        super(new Identifier(AuroraKeystrokes.NAMESPACE, "hud/keystrokes"));
        this.mod = mod;
    }

    @Override
    public void init(@NotNull MinecraftClient client, int screenWidth, int screenHeight)
    {
        super.init(client, screenWidth, screenHeight);
        this.client = client;
    }

    @Override
    public void render(MatrixStack matrices, float tickDelta)
    {
        int padding = this.mod.config.getPadding();
        String cpsText = "CPS: " + this.mod.cps;

        // Sizes.
        int boxHeight = this.client.textRenderer.fontHeight + padding * 2;
        int leftWidth = this.getBoxWidth(this.client.options.keyLeft);
        int backWidth = this.getBoxWidth(this.client.options.keyBack);
        int rightWidth = this.getBoxWidth(this.client.options.keyRight);
        int attackWidth = this.getBoxWidth(this.client.options.keyAttack);
        int useWidth = this.getBoxWidth(this.client.options.keyUse);
        int sneakWidth = this.getBoxWidth(this.client.options.keySneak);
        int jumpWidth = this.getBoxWidth(this.client.options.keyJump);
        int rightLeftWidth = leftWidth + padding + rightWidth;
        if (this.mod.config.getLayout() == LayoutMode.PYRAMID) {
            rightLeftWidth += padding + backWidth;
        }
        int mouseWidth = attackWidth + padding + useWidth;
        int jumpSneakWidth = sneakWidth + padding + jumpWidth;
        if (!this.mod.config.showMovementBoxes())
            rightLeftWidth = 0;
        if (!this.mod.config.showAttackBox() && !this.mod.config.showUseBox())
            mouseWidth = 0;
        else if (!this.mod.config.showAttackBox() || !this.mod.config.showUseBox()) {
            if (this.mod.config.showAttackBox())
                mouseWidth = attackWidth;
            else
                mouseWidth = useWidth;
        }
        if (!this.mod.config.showSneakBox() && !this.mod.config.showJumpBox())
            jumpSneakWidth = 0;
        else if (!this.mod.config.showSneakBox() || !this.mod.config.showJumpBox()) {
            if (this.mod.config.showSneakBox())
                jumpSneakWidth = sneakWidth;
            else
                jumpSneakWidth = jumpWidth;
        }
        int totalWidth = this.getTotalWidth(rightLeftWidth, mouseWidth, jumpSneakWidth);
        int totalHeight = this.getTotalHeight(boxHeight, padding);

        int x = (int) (this.mod.config.getX() / 100.0 * (this.client.getWindow().getScaledWidth() - totalWidth)),
                y = (int) (this.mod.config.getY() / 100.0 * (this.client.getWindow().getScaledHeight() - totalHeight));

        int boxWidth = this.getBoxWidth(this.client.options.keyForward);

        y -= (boxHeight + padding);

        // Movements keys.
        if (this.mod.config.showMovementBoxes()) {
            switch (this.mod.config.getLayout()) {
                case PYRAMID:
                    this.renderKeyBox(matrices, x + (totalWidth / 2 - boxWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyForward);
                    this.renderSection(matrices, x, (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyLeft, this.client.options.keyBack, this.client.options.keyRight, totalWidth, leftWidth, backWidth, rightWidth);
                    break;
                case CROSS:
                default:
                    this.renderKeyBox(matrices, x + (totalWidth / 2 - boxWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyForward);
                    this.renderSection(matrices, x, (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyLeft, this.client.options.keyRight, totalWidth, leftWidth, rightWidth);
                    this.renderKeyBox(matrices, x + (totalWidth / 2 - backWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyBack);
                    break;
            }
        }

        // Interaction keys.
        if (this.mod.config.showAttackBox() && this.mod.config.showUseBox())
            this.renderSection(matrices, x, (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyAttack, this.client.options.keyUse, totalWidth, attackWidth, useWidth);
        else if (this.mod.config.showAttackBox())
            this.renderKeyBox(matrices, x + (totalWidth / 2 - attackWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyAttack);
        else if (this.mod.config.showUseBox())
            this.renderKeyBox(matrices, x + (totalWidth / 2 - useWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyUse);

        // More movements keys.
        if (this.mod.config.showSneakBox() && this.mod.config.showJumpBox())
            this.renderSection(matrices, x, (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keySneak, this.client.options.keyJump, totalWidth, sneakWidth, jumpWidth);
        else if (this.mod.config.showSneakBox())
            this.renderKeyBox(matrices, x + (totalWidth / 2 - sneakWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keySneak);
        else if (this.mod.config.showJumpBox())
            this.renderKeyBox(matrices, x + (totalWidth / 2 - jumpWidth / 2), (y += (padding + boxHeight)), padding, boxHeight, this.client.options.keyJump);

        // CPS counter.
        if (this.mod.config.showCps()) {
            boxWidth = this.getBoxWidth(cpsText);
            int cpsX, cpsY;
            if (this.mod.config.attachedCps()) {
                cpsX = x + (totalWidth / 2 - boxWidth / 2);
                cpsY = y + (padding + boxHeight);
            } else {
                cpsX = this.client.getWindow().getScaledWidth() - padding - boxWidth;
                cpsY = this.client.getWindow().getScaledHeight() - padding - boxHeight;
            }
            AuroraKeystrokes.renderTextBox(matrices, this.client.textRenderer, cpsX, cpsY, padding, boxHeight, new LiteralText(cpsText), this.mod.config.getColorPressed(), this.mod.config.getBackgroundNormal());
        }
    }

    public int getTotalHeight(int boxHeight, int padding)
    {
        int i = 0;
        if (this.mod.config.showMovementBoxes())
            i += (boxHeight + padding) * 3;
        if (this.mod.config.showAttackBox() || this.mod.config.showUseBox())
            i += boxHeight + padding;
        if (this.mod.config.showSneakBox() || this.mod.config.showJumpBox())
            i += boxHeight + padding;
        if (this.mod.config.showCps() && this.mod.config.attachedCps())
            i += boxHeight + padding;
        return i - padding;
    }

    public int getTotalWidth(int rightLeftWidth, int mouseWidth, int jumpSneakWidth)
    {
        return Math.max(rightLeftWidth, Math.max(mouseWidth, jumpSneakWidth));
    }

    public int getBoxWidth(@NotNull KeyBinding keyBinding)
    {
        return this.getBoxWidth(this.mod.config.getTextDisplayMode().getText(keyBinding));
    }

    public int getBoxWidth(@NotNull Text text)
    {
        return this.client.textRenderer.getWidth(text) + this.mod.config.getPadding() * 2;
    }

    public int getBoxWidth(@NotNull String text)
    {
        return this.client.textRenderer.getWidth(text) + this.mod.config.getPadding() * 2;
    }

    public void renderSection(MatrixStack matrices, int x, int y, int padding, int boxHeight, @NotNull KeyBinding left, @NotNull KeyBinding right, int totalWidth, int leftWidth, int rightWidth)
    {
        if (totalWidth == leftWidth + padding + rightWidth) {
            leftWidth = this.renderKeyBox(matrices, x, y, padding, boxHeight, left);
            this.renderKeyBox(matrices, x + leftWidth + padding, y, padding, boxHeight, right);
        } else {
            int maxWidth = (totalWidth - padding) / 2;
            this.renderKeyBox(matrices, x + (maxWidth / 2 - leftWidth / 2), y, padding, boxHeight, left);
            this.renderKeyBox(matrices, x + padding + maxWidth + (maxWidth / 2 - rightWidth / 2), y, padding, boxHeight, right);
        }
    }

    public void renderSection(MatrixStack matrices, int x, int y, int padding, int boxHeight, @NotNull KeyBinding left, @NotNull KeyBinding center, @NotNull KeyBinding right, int totalWidth, int leftWidth, int centerWidth, int rightWidth)
    {
        int centerX = totalWidth / 2;
        int maxWidth = (totalWidth - padding) / 3;
        this.renderKeyBox(matrices, x + (centerX - leftWidth - padding - (centerWidth / 2)), y, padding, boxHeight, left);
        this.renderKeyBox(matrices, x + (centerX - (centerWidth / 2)), y, padding, boxHeight, center);
        this.renderKeyBox(matrices, x + (centerX + padding + (centerWidth / 2)), y, padding, boxHeight, right);
    }

    public int renderKeyBox(MatrixStack matrices, int x, int y, int padding, int boxHeight, @NotNull KeyBinding keyBinding)
    {
        if (keyBinding.isPressed())
            return AuroraKeystrokes.renderTextBox(matrices, this.client.textRenderer, x, y, padding, boxHeight, this.mod.config.getTextDisplayMode().getText(keyBinding), this.mod.config.getColorPressed(), this.mod.config.getBackgroundPressed());
        else
            return AuroraKeystrokes.renderTextBox(matrices, this.client.textRenderer, x, y, padding, boxHeight, this.mod.config.getTextDisplayMode().getText(keyBinding), this.mod.config.getColorNormal(), this.mod.config.getBackgroundNormal());
    }
}
