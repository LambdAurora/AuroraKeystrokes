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
import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.keystrokes.ColorConfigPanel;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents the color configuration screen.
 */
public class KeystrokesColorConfigScreen extends SpruceScreen {
    private final AuroraKeystrokes mod;
    private final Screen parent;
    private ColorConfigPanel panel;
    private final SpruceOption configPanelOption;
    private final SpruceOption frOption, fgOption, fbOption, faOption, brOption, bgOption, bbOption, baOption;
    private final SpruceOption rainbowText, rainbowSaturation;
    private int r = 0, g = 0, b = 0, a = 0;
    private int br = 0, bg = 0, bb = 0, ba = 0;
    private final java.util.List<SpruceButtonWidget> foregroundButtons = new ArrayList<>();
    private SpruceButtonWidget rainbowSaturationButton;

    protected KeystrokesColorConfigScreen(AuroraKeystrokes mod, Screen parent) {
        super(Text.translatable("keystrokes.menu.title.colors"));
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
                Text.translatable("keystrokes.tooltip.color_config_panel"));
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
                Text.translatable("keystrokes.tooltip.rainbow"));
        this.rainbowSaturation = new SpruceDoubleOption("keystrokes.menu.color.rainbow_saturation", 0.0, 1.0, 0.05F,
                this.mod.config::getRainbowSaturation,
                this.mod.config::setRainbowSaturation,
                option -> option.getDisplayText(Text.of(option.get() == 1.0 ? "1.0" : "0." + (int) (option.get() * 100))),
                Text.translatable("keystrokes.tooltip.rainbow_saturation"));
    }

    public void apply_rainbow(boolean newValue) {
        this.mod.config.setRainbowText(newValue);

        this.foregroundButtons.forEach(button -> button.setActive(newValue));
        if (this.rainbowSaturationButton != null)
            this.rainbowSaturationButton.setActive(newValue);
    }

    @Override
    public void closeScreen() {
        super.closeScreen();
        this.mod.config.save();
    }

    @Override
    protected void init() {
        super.init();
        this.mod.config.load();
        int widgetWidth = 204;
        int widgetHeight = 20;
        int margin = 4;
        int y = this.height / 4 - 8;
        this.initColors();
        this.initLeftWidgets(y, widgetWidth, widgetHeight, margin);
        this.initRightWidgets(y, widgetWidth, widgetHeight, margin);

        this.apply_rainbow(this.mod.config.useRainbowText());
    }

    private void initLeftWidgets(int y, int widgetWidth, int widgetHeight, int margin) {
        int x = (this.width / 4) - widgetWidth / 2;
        this.addDrawableChild(this.frOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.fgOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.fbOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.faOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addSelectableChild(this.configPanelOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addSelectableChild(this.rainbowText.createWidget(Position.of(x, (y + widgetHeight + margin)), widgetWidth));

    }

    private void initRightWidgets(int y, int widgetWidth, int widgetHeight, int margin) {
        int x = (this.width / 4) * 3 - widgetWidth / 2;
        this.addDrawableChild(this.brOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.bgOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.bbOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.baOption.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addDrawableChild(this.rainbowSaturation.createWidget(Position.of(x, (y += widgetHeight + margin)), widgetWidth));
        this.addSelectableChild(new SpruceButtonWidget(Position.of(x, (y + widgetHeight + margin)), widgetWidth, widgetHeight, SpruceTexts.GUI_DONE, (button) -> {
            this.applyColors();
            this.mod.config.save();
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }));
    }

    private void initColors() {
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

    private void applyColors() {
        this.panel.setColor(this.mod.config, new Color(r, g, b, a));
        this.panel.setBackgroundColor(this.mod.config, new Color(br, bg, bb, ba));
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 10, 16777215);
        var example = Text.translatable("keystrokes.menu.example_text");
        int y = this.height / 4 + 24 - 16;
        int padding = (20 - this.textRenderer.fontHeight) / 2;
        AuroraKeystrokes.renderTextBox(matrices, this.textRenderer, (this.width / 2 - this.textRenderer.getWidth(example)
                / 2), y / 2, padding, 20, example, new Color(r, g, b, a), new Color(br, bg, bb, ba));
    }
}
