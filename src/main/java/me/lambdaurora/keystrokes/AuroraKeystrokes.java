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

package me.lambdaurora.keystrokes;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aperlambda.lambdacommon.utils.LambdaUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Timer;

/**
 * Represents the mod instance of AuroraKeystrokes.
 */
public class AuroraKeystrokes implements ClientModInitializer
{
    private static AuroraKeystrokes INSTANCE;
    public final   Logger           logger = LogManager.getLogger("AuroraKeystrokes");
    public final   KeystrokesConfig config = new KeystrokesConfig(this);
    public         int              cps    = 0;

    @Override
    public void onInitializeClient()
    {
        INSTANCE = this;
        log("Initializing AuroraKeystrokes...");
        this.config.load();
        new Timer().scheduleAtFixedRate(LambdaUtils.new_timer_task_from_lambda(() -> this.cps = 0), 0, 1000);
    }

    /**
     * Prints a message to the console.
     *
     * @param info The message to print.
     */
    public void log(String info)
    {
        this.logger.info("[AuroraKeystrokes] " + info);
    }

    public static AuroraKeystrokes get()
    {
        return INSTANCE;
    }

    /**
     * Parses a color from a hexadecimal color string.
     *
     * @param hex The hexadecimal color.
     * @return The color instance, null if invalid.
     */
    public static Color parse_color(String hex)
    {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
        }
        return null;
    }

    /**
     * Renders a text box with a background.
     *
     * @param helper        The drawable helper.
     * @param text_renderer The text renderer.
     * @param x             The x position of the text box.
     * @param y             The y position of the text box.
     * @param padding       The padding of the box.
     * @param box_height    The box height.
     * @param text          The text inside the box.
     * @param foreground    The foreground color of the box.
     * @param background    The background color of the box.
     * @return The width of the box.
     */
    public static int render_text_box(DrawableHelper helper, TextRenderer text_renderer, int x, int y, int padding, int box_height, @NotNull String text, @NotNull Color foreground, @NotNull Color background)
    {
        int text_length = text_renderer.getStringWidth(text);
        int box_width = padding * 2 + text_length;
        DrawableHelper.fill(x, y, x + box_width, y + box_height, background.getRGB());
        helper.drawString(text_renderer, text, x + padding, y + padding + 1, foreground.getRGB());
        return box_width;
    }
}
