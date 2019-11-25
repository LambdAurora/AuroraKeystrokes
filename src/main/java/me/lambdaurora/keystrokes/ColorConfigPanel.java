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

import net.minecraft.client.resource.language.I18n;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents panels for the color config screen.
 */
public enum ColorConfigPanel implements Nameable
{
    NORMAL(),
    PRESSED() {
        public Color get_color(@NotNull KeystrokesConfig config)
        {
            return config.get_color_pressed();
        }

        public void set_color(@NotNull KeystrokesConfig config, @NotNull Color color)
        {
            config.set_color_pressed(color);
        }

        public Color get_background_color(@NotNull KeystrokesConfig config)
        {
            return config.get_background_pressed();
        }

        public void set_background_color(@NotNull KeystrokesConfig config, @NotNull Color color)
        {
            config.set_background_pressed(color);
        }
    };

    public Color get_color(@NotNull KeystrokesConfig config)
    {
        return config.get_color_normal();
    }

    public void set_color(@NotNull KeystrokesConfig config, @NotNull Color color)
    {
        config.set_color_normal(color);
    }

    public Color get_background_color(@NotNull KeystrokesConfig config)
    {
        return config.get_background_normal();
    }

    public void set_background_color(@NotNull KeystrokesConfig config, @NotNull Color color)
    {
        config.set_background_normal(color);
    }

    /**
     * Returns the next color config panel available.
     *
     * @return The next available color config panel.
     */
    public ColorConfigPanel next()
    {
        ColorConfigPanel[] v = values();
        if (v.length == this.ordinal() + 1)
            return v[0];
        return v[this.ordinal() + 1];
    }

    /**
     * Gets the translated name of this color config panel.
     *
     * @return The translated name of this color config panel.
     */
    public String get_translated_name()
    {
        return I18n.translate("keystrokes.color_config_panel." + this.get_name());
    }

    @Override
    public @NotNull String get_name()
    {
        return this.name().toLowerCase();
    }
}
