/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.SpruceTexts;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.option.SpruceCyclingOption;
import dev.lambdaurora.spruceui.option.SpruceDoubleOption;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceContainerWidget;
import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.ColorConfigPanel;
import me.lambdaurora.keystrokes.KeystrokesConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents the color configuration screen.
 */
public class KeystrokesColorConfigScreen extends SpruceScreen
{
    private final AuroraKeystrokes mod;
    private final Screen           parent;
    private       ColorConfigPanel panel;
    private final SpruceOption configPanelOption;
    private final SpruceOption           frOption, fgOption, fbOption, faOption, brOption, bgOption, bbOption, baOption;
    private final SpruceOption rainbowText, rainbowSaturation;
    private int r = 0, g = 0, b = 0, a = 0;
    private int br = 0, bg = 0, bb = 0, ba = 0;
    private final java.util.List<SpruceButtonWidget> foregroundButtons = new ArrayList<>();
    private       SpruceButtonWidget                 rainbowSaturationButton;

    protected KeystrokesColorConfigScreen(AuroraKeystrokes mod, Screen parent)
    {
        super(new TranslatableText("keystrokes.menu.title.colors"));
        this.mod = mod;
        this.parent = parent;
        this.panel = ColorConfigPanel.NORMAL;

        this.configPanelOption = new SpruceCyclingOption("keystrokes.menu.color_config_panel",
                amount -> {
                    this.applyColors();
                    this.panel = this.panel.next();
                    this.children().clear();
                    this.init();
                },
                option -> option.getDisplayText(this.panel.getText()),
                new TranslatableText("keystrokes.tooltip.color_config_panel"));
        this.frOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_r", 0.0, 255.0, 1.0F,
                () -> (double) this.r,
                newValue -> this.r = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.fgOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_g", 0.0, 255.0, 1.0F,
                () -> (double) this.g,
                newValue -> this.g = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.fbOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_b", 0.0, 255.0, 1.0F,
                () -> (double) this.b,
                newValue -> this.b = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.faOption = new SpruceDoubleOption("keystrokes.menu.color_foreground_a", 0.0, 255.0, 1.0F,
                () -> (double) this.a,
                newValue -> this.a = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.brOption = new SpruceDoubleOption("keystrokes.menu.color_background_r", 0.0, 255.0, 1.0F,
                () -> (double) this.br,
                newValue -> this.br = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.bgOption = new SpruceDoubleOption("keystrokes.menu.color_background_g", 0.0, 255.0, 1.0F,
                () -> (double) this.bg,
                newValue -> this.bg = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.bbOption = new SpruceDoubleOption("keystrokes.menu.color_background_b", 0.0, 255.0, 1.0F,
                () -> (double) this.bb,
                newValue -> this.bb = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.baOption = new SpruceDoubleOption("keystrokes.menu.color_background_a", 0.0, 255.0, 1.0F,
                () -> (double) this.ba,
                newValue -> this.ba = newValue.intValue(),
                option -> option.getDisplayText(Text.of(String.valueOf((int) option.get()))),
                null);
        this.rainbowText = new SpruceBooleanOption("keystrokes.menu.color.rainbow",
                this.mod.config::useRainbowText,
                this::apply_rainbow,
                new TranslatableText("keystrokes.tooltip.rainbow"));
        this.rainbowSaturation = new SpruceDoubleOption("keystrokes.menu.color.rainbow_saturation", 0.0, 1.0, 0.05F,
                this.mod.config::getRainbowSaturation,
                this.mod.config::setRainbowSaturation,
                option -> option.getDisplayText(Text.of(option.get() == 1.0 ? "1.0" : "0." + (int) (option.get() * 100))),
                new TranslatableText("keystrokes.tooltip.rainbow_saturation"));
    }

    public void apply_rainbow(boolean newValue)
    {
        this.mod.config.setRainbowText(newValue);

        this.foregroundButtons.forEach(button -> button.setActive(newValue));
        if (this.rainbowSaturationButton != null)
            this.rainbowSaturationButton.setActive(newValue);
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
        this.initColors();
        this.initLeftWidgets(widgetWidth, widgetHeight, margin);
        this.initRightWidgets(widgetWidth, widgetHeight, margin);

        this.apply_rainbow(this.mod.config.useRainbowText());
    }

    private void initLeftWidgets(int widgetWidth, int widgetHeight, int margin)
    {
        var container = new SpruceContainerWidget(Position.origin(), width, (widgetHeight + margin) * 4); // Width/height and position may need to be tweaked
        container.addChild(this.frOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.fgOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.fbOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.faOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.configPanelOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.rainbowText.createWidget(Position.origin(), widgetWidth));
        this.addDrawableChild(container);
    }

    private void initRightWidgets(int widgetWidth, int widgetHeight, int margin)
    {
        var container = new SpruceContainerWidget(Position.origin(), width, (widgetHeight + margin) * 4);
        container.addChild(this.brOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.brOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.brOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.brOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(this.brOption.createWidget(Position.origin(), widgetWidth));
        container.addChild(new SpruceButtonWidget(Position.origin(), widgetWidth, widgetHeight, SpruceTexts.GUI_DONE, (button) -> {
            this.applyColors();
            this.mod.config.save();
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
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
            case 'r' -> setter.accept(this.mod.config, new Color(rgbaPart, color.getGreen(), color.getBlue(), color.getAlpha()));
            case 'g' -> setter.accept(this.mod.config, new Color(color.getRed(), rgbaPart, color.getBlue(), color.getAlpha()));
            case 'b' -> setter.accept(this.mod.config, new Color(color.getRed(), color.getGreen(), rgbaPart, color.getAlpha()));
            case 'a' -> setter.accept(this.mod.config, new Color(color.getRed(), color.getGreen(), color.getBlue(), rgbaPart));
        }
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 10, 16777215);
    }
}
