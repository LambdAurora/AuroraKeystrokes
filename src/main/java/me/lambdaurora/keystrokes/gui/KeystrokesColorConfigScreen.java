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
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.option.SpruceCyclingOption;
import me.lambdaurora.spruceui.option.SpruceDoubleOption;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.Option;
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
    private final AuroraKeystrokes mod;
    private final Screen           parent;
    private       ColorConfigPanel panel;
    private final Option           configPanelOption;
    private final Option           frOption, fgOption, fbOption, faOption, brOption, bgOption, bbOption, baOption;
    private int r = 0, g = 0, b = 0, a = 0;
    private int br = 0, bg = 0, bb = 0, ba = 0;

    protected KeystrokesColorConfigScreen(@NotNull AuroraKeystrokes mod, Screen parent)
    {
        super(new TranslatableText("keystrokes.menu.title.colors"));
        this.mod = mod;
        this.parent = parent;
        this.panel = ColorConfigPanel.NORMAL;

        this.configPanelOption = new SpruceCyclingOption("keystrokes.menu.color_config_panel",
                amount -> {
                    this.applyColors();
                    this.panel = this.panel.next();
                    this.buttons.clear();
                    this.children.clear();
                    this.init();
                },
                option -> option.getDisplayPrefix() + this.panel.getTranslatedName(),
                new TranslatableText("keystrokes.tooltip.color_config_panel"));
        this.frOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_r", 0.0, 255.0, 1.0F,
                () -> (double) this.r,
                newValue -> this.r = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.fgOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_g", 0.0, 255.0, 1.0F,
                () -> (double) this.g,
                newValue -> this.g = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.fbOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_b", 0.0, 255.0, 1.0F,
                () -> (double) this.b,
                newValue -> this.b = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.faOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_a", 0.0, 255.0, 1.0F,
                () -> (double) this.a,
                newValue -> this.a = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.brOption = new SpruceDoubleOption("keystrokes.menu.color_background_r", 0.0, 255.0, 1.0F,
                () -> (double) this.br,
                newValue -> this.br = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.bgOption = new SpruceDoubleOption("keystrokes.menu.color_background_g", 0.0, 255.0, 1.0F,
                () -> (double) this.bg,
                newValue -> this.bg = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.bbOption = new SpruceDoubleOption("keystrokes.menu.color_background_b", 0.0, 255.0, 1.0F,
                () -> (double) this.bb,
                newValue -> this.bb = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
        this.baOption = new SpruceDoubleOption("keystrokes.menu.color_background_a", 0.0, 255.0, 1.0F,
                () -> (double) this.ba,
                newValue -> this.ba = newValue.intValue(),
                option -> option.getDisplayPrefix() + (int) option.get(),
                null);
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
        int widgetWidth = 204;
        int widgetHeight = 20;
        int margin = 4;
        int y = this.height / 4 + 24 + -16;
        this.initColors();
        this.initLeftWidgets(y, widgetWidth, widgetHeight, margin);
        this.initRightWidgets(y, widgetWidth, widgetHeight, margin);
    }

    private void initLeftWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        int x = this.width / 4 - widgetWidth / 2;
        this.addButton(this.frOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.fgOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.fbOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.faOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.configPanelOption.createButton(null, x, y + widgetHeight + margin, widgetWidth));
    }

    private void initRightWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        int x = (this.width / 4) * 3 - widgetWidth / 2;
        this.addButton(this.brOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.bgOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.bbOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.baOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(new ButtonWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("gui.done"), (button) -> {
            this.applyColors();
            this.mod.config.save();
            this.minecraft.openScreen(this.parent);
        }));
    }

    private void initColors()
    {
        Color foreground = this.panel.getColor(this.mod.config);
        this.r = foreground.getRed();
        this.g = foreground.getGreen();
        this.b = foreground.getBlue();
        this.a = foreground.getAlpha();
        Color background = this.panel.getBackgroundColor(this.mod.config);
        this.br = background.getRed();
        this.bg = background.getGreen();
        this.bb = background.getBlue();
        this.ba = background.getAlpha();
    }

    private void applyColors()
    {
        this.panel.setColor(this.mod.config, new Color(r, g, b, a));
        this.panel.setBackgroundColor(this.mod.config, new Color(br, bg, bb, ba));
    }

    private void applyColor(@NotNull Function<KeystrokesConfig, Color> getter, @NotNull BiConsumer<KeystrokesConfig, Color> setter, double value, char part)
    {
        Color color = getter.apply(this.mod.config);
        int rgbaPart = (int) (value * 255.0);
        switch (part) {
            case 'r':
                setter.accept(this.mod.config, new Color(rgbaPart, color.getGreen(), color.getBlue(), color.getAlpha()));
                break;
            case 'g':
                setter.accept(this.mod.config, new Color(color.getRed(), rgbaPart, color.getBlue(), color.getAlpha()));
                break;
            case 'b':
                setter.accept(this.mod.config, new Color(color.getRed(), color.getGreen(), rgbaPart, color.getAlpha()));
                break;
            case 'a':
                setter.accept(this.mod.config, new Color(color.getRed(), color.getGreen(), color.getBlue(), rgbaPart));
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
        AuroraKeystrokes.renderTextBox(this, this.font, (this.width / 2 - this.font.getStringWidth(example) / 2), y / 2, padding, 20, example, new Color(r, g, b, a), new Color(br, bg, bb, ba));

        super.render(mouseX, mouseY, delta);

        Tooltip.renderAll();
    }
}
