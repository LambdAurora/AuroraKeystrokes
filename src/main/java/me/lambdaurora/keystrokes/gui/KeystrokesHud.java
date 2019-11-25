/*
 * FabricKeystrokes
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

package me.lambdaurora.keystrokes.gui;

import me.lambdaurora.keystrokes.FabricKeystrokes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.options.KeyBinding;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents the keystrokes hud.
 */
public class KeystrokesHud extends DrawableHelper
{
    private final MinecraftClient  client;
    private final FabricKeystrokes mod;

    public KeystrokesHud(MinecraftClient client, FabricKeystrokes mod)
    {
        this.client = client;
        this.mod = mod;
    }

    public void render()
    {
        int x = (int) (this.mod.config.get_x() / 100.0 * this.client.getWindow().getScaledWidth()),
                y = (int) (this.mod.config.get_y() / 100.0 * this.client.getWindow().getScaledHeight());
        int padding = this.mod.config.get_padding();
        String cps_text = "CPS: " + this.mod.cps;

        // Sizes.
        int box_height = this.client.textRenderer.fontHeight + padding * 2;
        int left_width = this.get_box_width(this.client.options.keyLeft);
        int right_width = this.get_box_width(this.client.options.keyRight);
        int attack_width = this.get_box_width(this.client.options.keyAttack);
        int use_width = this.get_box_width(this.client.options.keyUse);
        int sneak_width = this.get_box_width(this.client.options.keySneak);
        int jump_width = this.get_box_width(this.client.options.keyJump);
        int right_left_width = left_width + padding + right_width;
        int mouse_width = attack_width + padding + use_width;
        int jump_sneak_width = sneak_width + padding + jump_width;
        if (!this.mod.config.show_movement_boxes())
            right_left_width = 0;
        if (!this.mod.config.show_interaction_boxes())
            mouse_width = 0;
        if (!this.mod.config.show_sneak_box() && !this.mod.config.show_jump_box())
            jump_sneak_width = 0;
        else if (!this.mod.config.show_sneak_box() || !this.mod.config.show_jump_box()) {
            if (this.mod.config.show_sneak_box())
                jump_sneak_width = sneak_width;
            else
                jump_sneak_width = jump_width;
        }
        int total_width = this.get_total_width(right_left_width, mouse_width, jump_sneak_width);
        int box_width = this.get_box_width(this.client.options.keyForward);

        int line = 0;

        // Movements keys.
        if (this.mod.config.show_movement_boxes()) {
            this.render_key_box(x + (total_width / 2 - box_width / 2), y + (padding + box_height) * ++line, box_height, this.client.options.keyForward);
            this.render_section(x, y + (padding + box_height) * ++line, padding, box_height, this.client.options.keyLeft, this.client.options.keyRight, total_width, left_width, right_width);
            box_width = this.get_box_width(this.client.options.keyBack);
            this.render_key_box(x + (total_width / 2 - box_width / 2), y + (padding + box_height) * ++line, box_height, this.client.options.keyBack);
        }

        // Interaction keys.
        if (this.mod.config.show_interaction_boxes())
            this.render_section(x, y + (padding + box_height) * ++line, padding, box_height, this.client.options.keyAttack, this.client.options.keyUse, total_width, attack_width, use_width);

        // More movements keys.
        if (this.mod.config.show_sneak_box() && this.mod.config.show_jump_box())
            this.render_section(x, y + (padding + box_height) * ++line, padding, box_height, this.client.options.keySneak, this.client.options.keyJump, total_width, sneak_width, jump_width);
        else if (this.mod.config.show_sneak_box()) {
            box_width = this.get_box_width(this.client.options.keySneak);
            this.render_key_box(x + (total_width / 2 - box_width / 2), y + (padding + box_height) * ++line, box_height, this.client.options.keySneak);
        } else if (this.mod.config.show_jump_box()) {
            box_width = this.get_box_width(this.client.options.keyJump);
            this.render_key_box(x + (total_width / 2 - box_width / 2), y + (padding + box_height) * ++line, box_height, this.client.options.keyJump);
        }

        // CPS counter.
        if (this.mod.config.show_cps()) {
            box_width = this.get_box_width(cps_text);
            int cps_x, cps_y;
            if (this.mod.config.attached_cps()) {
                cps_x = x + (total_width / 2 - box_width / 2);
                cps_y = y + (padding + box_height) * ++line;
            } else {
                cps_x = this.client.getWindow().getScaledWidth() - padding - box_width;
                cps_y = this.client.getWindow().getScaledHeight() - padding - box_height;
            }
            this.render_text_box(cps_x, cps_y, box_height, cps_text, this.mod.config.get_color_pressed(), this.mod.config.get_background_normal());
        }
    }

    public int get_total_width(int right_left_width, int mouse_width, int jump_sneak_width)
    {
        return Math.max(right_left_width, Math.max(mouse_width, jump_sneak_width));
    }

    public int get_box_width(@NotNull KeyBinding key_binding)
    {
        return this.get_box_width(this.mod.config.get_text_display_mode().get_text(key_binding));
    }

    public int get_box_width(@NotNull String text)
    {
        return this.client.textRenderer.getStringWidth(text) + this.mod.config.get_padding() * 2;
    }

    public void render_section(int x, int y, int padding, int box_height, @NotNull KeyBinding left, @NotNull KeyBinding right, int total_width, int left_width, int right_width)
    {
        if (total_width == left_width + padding + right_width) {
            left_width = this.render_key_box(x, y, box_height, left);
            this.render_key_box(x + left_width + padding, y, box_height, right);
        } else {
            int max_width = (total_width - padding) / 2;
            this.render_key_box(x + (max_width / 2 - left_width / 2), y, box_height, left);
            this.render_key_box(x + padding + max_width + (max_width / 2 - right_width / 2), y, box_height, right);
        }
    }

    public int render_key_box(int x, int y, int box_height, @NotNull KeyBinding key_binding)
    {
        if (key_binding.isPressed())
            return render_text_box(x, y, box_height, this.mod.config.get_text_display_mode().get_text(key_binding), this.mod.config.get_color_pressed(), this.mod.config.get_background_pressed());
        else
            return render_text_box(x, y, box_height, this.mod.config.get_text_display_mode().get_text(key_binding), this.mod.config.get_color_normal(), this.mod.config.get_background_normal());
    }

    public int render_text_box(int x, int y, int box_height, @NotNull String text, @NotNull Color foreground, @NotNull Color background)
    {
        int padding = this.mod.config.get_padding();
        int text_length = this.client.textRenderer.getStringWidth(text);
        int box_width = padding * 2 + text_length;
        fill(x, y, x + box_width, y + box_height, background.getRGB());
        this.drawString(this.client.textRenderer, text, x + padding, y + padding + 1, foreground.getRGB());
        return box_width;
    }
}
