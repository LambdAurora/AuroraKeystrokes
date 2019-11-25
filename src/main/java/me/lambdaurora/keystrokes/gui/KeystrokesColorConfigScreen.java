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

package me.lambdaurora.keystrokes.gui;

import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.ColorConfigPanel;
import me.lambdaurora.keystrokes.KeystrokesConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents the color configuration screen.
 */
public class KeystrokesColorConfigScreen extends Screen
{
    private final AuroraKeystrokes       mod;
    private final Screen                 parent;
    private       ColorConfigPanel       panel;
    private       KeystrokesSliderWidget widget_fr, widget_fg, widget_fb, widget_fa, widget_br, widget_bg, widget_bb, widget_ba;
    private int r = 0, g = 0, b = 0, a = 0;
    private int br = 0, bg = 0, bb = 0, ba = 0;

    protected KeystrokesColorConfigScreen(@NotNull AuroraKeystrokes mod, Screen parent)
    {
        super(new TranslatableText("keystrokes.menu.title.colors"));
        this.mod = mod;
        this.parent = parent;
        this.panel = ColorConfigPanel.NORMAL;
    }

    @Override
    public void onClose()
    {
        super.onClose();
        this.mod.config.save();
    }

    @Override
    protected void init()
    {
        super.init();
        this.mod.config.load();
        int widget_width = 204;
        int widget_height = 20;
        int margin = 4;
        int y = this.height / 4 + 24 + -16;
        this.init_left_widgets(y, widget_width, widget_height, margin);
        this.init_right_widgets(y, widget_width, widget_height, margin);
        this.init_colors();
    }

    private void init_left_widgets(int y, int widget_width, int widget_height, int margin)
    {
        int x = this.width / 4 - widget_width / 2;
        Color normal_color = this.panel.get_color(this.mod.config);
        this.addButton((this.widget_fr = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_foreground_r"),
                normal_color.getRed() / 255.0, btn -> r = btn.get_int_value(),
                255.0, "")));
        this.addButton((this.widget_fg = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_foreground_g"),
                normal_color.getGreen() / 255.0, btn -> g = btn.get_int_value(),
                255.0, "")));
        this.addButton((this.widget_fb = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_foreground_b"),
                normal_color.getBlue() / 255.0, btn -> b = btn.get_int_value(),
                255.0, "")));
        this.addButton((this.widget_fa = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_foreground_a"),
                normal_color.getAlpha() / 255.0, btn -> a = btn.get_int_value(),
                255.0, "")));
        this.addButton(new ButtonWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_config_panel") + ": " + this.panel.get_translated_name(), btn -> {
            ColorConfigPanel next = this.panel.next();
            btn.setMessage(I18n.translate("keystrokes.menu.color_config_panel") + next.get_translated_name());
            this.apply_colors();
            this.panel = next;
            this.init_colors();
        }));
    }

    private void init_right_widgets(int y, int widget_width, int widget_height, int margin)
    {
        int x = (this.width / 4) * 3 - widget_width / 2;
        Color background_color = this.panel.get_background_color(this.mod.config);
        this.addButton((this.widget_br = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_background_r"),
                background_color.getRed() / 255.0, btn -> br = btn.get_int_value(),
                255.0, "")));
        this.addButton((this.widget_bg = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_background_g"),
                background_color.getGreen() / 255.0, btn -> bg = btn.get_int_value(),
                255.0, "")));
        this.addButton((this.widget_bb = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_background_b"),
                background_color.getBlue() / 255.0, btn -> bb = btn.get_int_value(),
                255.0, "")));
        this.addButton((this.widget_ba = new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.color_background_a"),
                background_color.getAlpha() / 255.0, btn -> ba = btn.get_int_value(),
                255.0, "")));
        this.addButton(new ButtonWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("gui.done"), (button) -> {
            this.apply_colors();
            this.mod.config.save();
            this.minecraft.openScreen(this.parent);
        }));
    }

    private void init_colors()
    {
        Color foreground = this.panel.get_color(this.mod.config);
        this.r = foreground.getRed();
        this.g = foreground.getGreen();
        this.b = foreground.getBlue();
        this.a = foreground.getAlpha();
        this.widget_fr.set_int_value(this.r);
        this.widget_fg.set_int_value(this.g);
        this.widget_fb.set_int_value(this.b);
        this.widget_fa.set_int_value(this.a);
        Color background = this.panel.get_background_color(this.mod.config);
        this.br = background.getRed();
        this.bg = background.getGreen();
        this.bb = background.getBlue();
        this.ba = background.getAlpha();
        this.widget_br.set_int_value(this.br);
        this.widget_bg.set_int_value(this.bg);
        this.widget_bb.set_int_value(this.bb);
        this.widget_ba.set_int_value(this.ba);
    }

    private void apply_colors()
    {
        this.panel.set_color(this.mod.config, new Color(r, g, b, a));
        this.panel.set_background_color(this.mod.config, new Color(br, bg, bb, ba));
    }

    private void apply_color(@NotNull Function<KeystrokesConfig, Color> getter, @NotNull BiConsumer<KeystrokesConfig, Color> setter, double value, char part)
    {
        Color color = getter.apply(this.mod.config);
        int rgba_part = (int) (value * 255.0);
        switch (part) {
            case 'r':
                setter.accept(this.mod.config, new Color(rgba_part, color.getGreen(), color.getBlue(), color.getAlpha()));
                break;
            case 'g':
                setter.accept(this.mod.config, new Color(color.getRed(), rgba_part, color.getBlue(), color.getAlpha()));
                break;
            case 'b':
                setter.accept(this.mod.config, new Color(color.getRed(), color.getGreen(), rgba_part, color.getAlpha()));
                break;
            case 'a':
                setter.accept(this.mod.config, new Color(color.getRed(), color.getGreen(), color.getBlue(), rgba_part));
                break;
        }
    }

    public void render(int mouseX, int mouseY, float delta)
    {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 10, 16777215);

        String example = I18n.translate("keystrokes.menu.example_text");
        int y = this.height / 4 + 24 + -16;
        int padding = (20 - this.font.fontHeight) / 2;
        AuroraKeystrokes.render_text_box(this, this.font, (this.width / 2 - this.font.getStringWidth(example) / 2), y / 2, padding, 20, example, new Color(r, g, b, a), new Color(br, bg, bb, ba));

        super.render(mouseX, mouseY, delta);
    }
}
