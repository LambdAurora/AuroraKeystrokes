/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes;

import dev.lambdaurora.spruceui.hud.HudManager;
import me.lambdaurora.keystrokes.command.KeystrokesCommand;
import me.lambdaurora.keystrokes.gui.KeystrokesHud;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.command.api.client.ClientCommandManager;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import java.awt.*;

/**
 * Represents the mod instance of AuroraKeystrokes.
 *
 * @author LambdAurora
 * @version 1.2.4
 * @since 1.0.0
 */
public class AuroraKeystrokes implements ClientModInitializer {
    public static final String NAMESPACE = "aurorakeystrokes";
    private static AuroraKeystrokes INSTANCE;
    public final Logger logger = LogManager.getLogger("AuroraKeystrokes");
    public final KeystrokesConfig config = new KeystrokesConfig(this);
    private KeystrokesHud hud;
    public int cps = 0;
    private long lastTime = 0;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        log("Initializing AuroraKeystrokes...");
        this.config.load();
        ClientTickEvents.START.register(client -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) {
                this.cps = 0;
                this.lastTime = currentTime;
            }
        });

        HudManager.register(this.hud = new KeystrokesHud(this));
        this.hud.setVisible(this.config.doesRenderHud());

        KeystrokesCommand.registerCommands(ClientCommandManager.getDispatcher());
    }

    /**
     * Prints a message to the console.
     *
     * @param info The message to print.
     */
    public void log(String info) {
        this.logger.info("[AuroraKeystrokes] " + info);
    }

    public static AuroraKeystrokes get() {
        return INSTANCE;
    }

    /**
     * Sets whether the HUD is enabled or not.
     *
     * @param enabled True if the HUD is enabled, else false.
     */
    public void setHudEnabled(boolean enabled) {
        this.config.setRenderHud(enabled);
        this.hud.setVisible(enabled);
    }

    /**
     * Parses a color from a hexadecimal color string.
     *
     * @param hex The hexadecimal color.
     * @return The color instance, null if invalid.
     */
    public static Color parseColor(String hex) {
        hex = hex.replace("#", "");
        return switch (hex.length()) {
            case 6 -> new Color(
                    Integer.valueOf(hex.substring(0, 2), 16),
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16));
            case 8 -> new Color(
                    Integer.valueOf(hex.substring(0, 2), 16),
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16),
                    Integer.valueOf(hex.substring(6, 8), 16));
            default -> null;
        };
    }

    public static int getRainbowRGB(double x, double y) {
        float speed = 2600.0F;
        return Color.HSBtoRGB((float) ((System.currentTimeMillis() - x * 10.0D - y * 10.0D) % speed) / speed,
                (float) AuroraKeystrokes.get().config.getRainbowSaturation(),
                0.9F);
    }

    public static void drawRainbowString(MatrixStack matrices, TextRenderer textRenderer, int x, int y, @NotNull String text) {
        for (char c : text.toCharArray()) {
            int rgb = getRainbowRGB(x, y);
            String tmp = String.valueOf(c);
            textRenderer.draw(matrices, tmp, x, y, rgb);
            x += textRenderer.getWidth(tmp);
        }
    }

    /**
     * Renders a text box with a background.
     *
     * @param textRenderer The text renderer.
     * @param x            The x position of the text box.
     * @param y            The y position of the text box.
     * @param padding      The padding of the box.
     * @param boxHeight    The box height.
     * @param text         The text inside the box.
     * @param foreground   The foreground color of the box.
     * @param background   The background color of the box.
     * @return The width of the box.
     */
    public static int renderTextBox(MatrixStack matrices, TextRenderer textRenderer, int x, int y, int padding, int boxHeight, @NotNull Text text, @NotNull Color foreground, @NotNull Color background) {
        int textLength = textRenderer.getWidth(text);
        int boxWidth = padding * 2 + textLength;
        DrawableHelper.fill(matrices, x, y, x + boxWidth, y + boxHeight, background.getRGB());
        if (AuroraKeystrokes.get().config.useRainbowText())
            drawRainbowString(matrices, textRenderer, x + padding, y + padding + 1, text.getString());
        else
            DrawableHelper.drawTextWithShadow(matrices, textRenderer, text, x + padding, y + padding + 1, foreground.getRGB());
        return boxWidth;
    }
}
