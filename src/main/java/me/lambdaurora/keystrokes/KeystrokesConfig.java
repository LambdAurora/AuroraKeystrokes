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

import com.electronwill.nightconfig.core.file.FileConfig;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents the AuroraKeytrokes configuration.
 */
public class KeystrokesConfig
{
    private final FileConfig       config            = FileConfig.builder("config/keystrokes.toml").concurrent().defaultResource("/config.toml").build();
    private final AuroraKeystrokes mod;
    private       TextDisplayMode  text_display_mode = TextDisplayMode.ACTION_NAME;

    public KeystrokesConfig(@NotNull AuroraKeystrokes mod)
    {
        this.mod = mod;
    }

    public void load()
    {
        this.config.load();
        this.text_display_mode = TextDisplayMode.by_id(this.config.getOrElse("hud.text_display_mode", "action_name")).orElseGet(() -> {
            this.mod.log("Could not load `text_display_mode` property: invalid value.");
            return TextDisplayMode.ACTION_NAME;
        });
    }

    public void save()
    {
        this.config.save();
    }

    public boolean does_render_hud()
    {
        return this.config.getOrElse("hud.enable", true);
    }

    public void set_render_hud(boolean render_hud)
    {
        this.config.set("hud.enable", render_hud);
    }

    public double get_x()
    {
        return this.config.getOrElse("hud.x", 10.0);
    }

    public void set_x(double x)
    {
        this.config.set("hud.x", x);
    }

    public double get_y()
    {
        return this.config.getOrElse("hud.y", 10.0);
    }

    public void set_y(double y)
    {
        this.config.set("hud.y", y);
    }

    public int get_padding()
    {
        return this.config.getOrElse("hud.padding", 4);
    }

    public void set_padding(int padding)
    {
        this.config.set("hud.padding", padding);
    }

    public boolean show_movement_boxes()
    {
        return this.config.getOrElse("hud.show_movement_boxes", true);
    }

    public void set_movement_boxes(boolean show)
    {
        this.config.set("hud.show_movement_boxes", show);
    }

    public boolean show_attack_box()
    {
        return this.config.getOrElse("hud.show_attack_box", true);
    }

    public void set_attack_box(boolean show)
    {
        this.config.set("hud.show_attack_box", show);
    }

    public boolean show_use_box()
    {
        return this.config.getOrElse("hud.show_use_box", true);
    }

    public void set_use_box(boolean show)
    {
        this.config.set("hud.show_use_box", show);
    }

    public boolean show_sneak_box()
    {
        return this.config.getOrElse("hud.show_sneak_box", true);
    }

    public void set_sneak_box(boolean show)
    {
        this.config.set("hud.show_sneak_box", show);
    }

    public boolean show_jump_box()
    {
        return this.config.getOrElse("hud.show_jump_box", true);
    }

    public void set_jump_box(boolean show)
    {
        this.config.set("hud.show_jump_box", show);
    }

    public TextDisplayMode get_text_display_mode()
    {
        return this.text_display_mode;
    }

    public void set_text_display_mode(TextDisplayMode text_display_mode)
    {
        if (text_display_mode != this.text_display_mode) {
            this.text_display_mode = text_display_mode;
            this.config.set("hud.text_display_mode", text_display_mode.name().toLowerCase());
        }
    }

    public boolean show_cps()
    {
        return this.config.getOrElse("cps.show", true);
    }

    public void set_show_cps(boolean show)
    {
        this.config.set("cps.show", show);
    }

    public boolean attached_cps()
    {
        return this.config.getOrElse("cps.attached", true);
    }

    public void set_attached_cps(boolean attached)
    {
        this.config.set("cps.attached", attached);
    }

    private String hex(int i)
    {
        String res = Integer.toHexString(i);
        if (res.length() == 1)
            return "0" + res;
        return res;
    }

    public Color get_color_normal()
    {
        return AuroraKeystrokes.parse_color(this.config.getOrElse("colors.normal", "#FFFFFFFF"));
    }

    public void set_color_normal(@NotNull Color color)
    {
        this.config.set("colors.normal", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public Color get_color_pressed()
    {
        return AuroraKeystrokes.parse_color(this.config.getOrElse("colors.pressed", "#FFA000FF"));
    }

    public void set_color_pressed(@NotNull Color color)
    {
        this.config.set("colors.pressed", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public Color get_background_normal()
    {
        return AuroraKeystrokes.parse_color(this.config.getOrElse("colors.background_normal", "#000000AA"));
    }

    public void set_background_normal(@NotNull Color color)
    {
        this.config.set("colors.background_normal", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public Color get_background_pressed()
    {
        return AuroraKeystrokes.parse_color(this.config.getOrElse("colors.background_pressed", "#FFFFFFAA"));
    }

    public void set_background_pressed(@NotNull Color color)
    {
        this.config.set("colors.background_pressed", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }
}
