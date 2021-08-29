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
import dev.lambdaurora.spruceui.option.*;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.lambdaurora.keystrokes.AuroraKeystrokes;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the config screen of AuroraKeystrokes.
 */
public class KeystrokesConfigScreen extends SpruceScreen
{
    private final AuroraKeystrokes mod;

    private final SpruceOption resetOption;

    private final List<SpruceOption> leftOptions;
    private final List<SpruceOption> rightOptions;

    public KeystrokesConfigScreen(@NotNull AuroraKeystrokes mod)
    {
        super(new TranslatableText("keystrokes.menu.title.main"));
        this.mod = mod;

        this.leftOptions = new ArrayList<>();

        this.leftOptions.add(new SpruceDoubleOption("keystrokes.menu.x", 0.0, 100.0, 0.5F,
                this.mod.config::getX,
                this.mod.config::setX,
                option -> new LiteralText("X: " + option.get()),
                new TranslatableText("keystrokes.tooltip.x")));
        this.leftOptions.add(new SpruceDoubleOption("keystrokes.menu.y", 0.0, 100.0, 0.5F,
                this.mod.config::getY,
                this.mod.config::setY,
                (option) -> new LiteralText("Y: " + option.get()),
                new TranslatableText("keystrokes.tooltip.y")));
        this.leftOptions.add(new SpruceDoubleOption("keystrokes.menu.padding", 0.0, 20.0, 1.0F,
                () -> (double) this.mod.config.getPadding(),
                newValue -> this.mod.config.setPadding(newValue.intValue()),
                (option) -> option.getDisplayText(Text.of(String.valueOf(option.get()))),
                new TranslatableText("keystrokes.tooltip.padding")));

        /* Boxes */
        this.leftOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.show_movement_boxes",
                this.mod.config::showMovementBoxes, this.mod.config::setMovementBoxes, null));
        this.leftOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.show_attack_box",
                this.mod.config::showAttackBox, this.mod.config::setAttackBox, null));
        this.leftOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.show_use_box",
                this.mod.config::showUseBox, this.mod.config::setUseBox, null));
        this.leftOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.show_sneak_box",
                this.mod.config::showSneakBox, this.mod.config::setSneakBox, null));
        this.leftOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.show_jump_box",
                this.mod.config::showJumpBox, this.mod.config::setJumpBox, null));

        this.rightOptions = new ArrayList<>();

        this.rightOptions.add(new SpruceCyclingOption("keystrokes.menu.text_display_mode",
                amount -> this.mod.config.setTextDisplayMode(this.mod.config.getTextDisplayMode().next()),
                option -> option.getDisplayText(this.mod.config.getTextDisplayMode().getText()),
                new TranslatableText("keystrokes.tooltip.text_display_mode")));

        /* CPS */
        this.rightOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.show_cps",
                this.mod.config::showCps, this.mod.config::setShowCps, null));
        this.rightOptions.add(new SpruceCheckboxBooleanOption("keystrokes.menu.attach_cps",
                this.mod.config::attachedCps, this.mod.config::setAttachedCps, null));

        this.rightOptions.add(new SpruceCyclingOption("keystrokes.menu.layout",
                amount -> this.mod.config.setLayout(this.mod.config.getLayout().next()),
                option -> option.getDisplayText(new TranslatableText("keystrokes.layout." + this.mod.config.getLayout().getName())),
                null));

        this.resetOption = SpruceSimpleActionOption.reset(btn -> {
            if (this.client != null) {
                this.client.setScreen(new ConfirmScreen(confirm -> {
                    if (confirm) {
                        if (new File("config/keystrokes.toml").delete()) {
                            this.mod.config.load();
                            this.client.setScreen(new KeystrokesConfigScreen(this.mod));
                        } else {
                            this.client.setScreen(new FatalErrorScreen(SpruceTexts.RESET_TEXT, new TranslatableText("keystrokes.error.cannot_reset")));
                        }
                    } else this.client.setScreen(this);
                }, SpruceTexts.RESET_TEXT, new TranslatableText("keystrokes.menu.confirm_reset")));
            }
        });
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
        int y = this.height / 4 + 24 + -32;
        TranslatableText showHudText = new TranslatableText("keystrokes.menu.show_hud");
        this.addDrawableChild(new SpruceCheckboxBooleanOption((this.width / 2 - (24 + this.textRenderer.getWidth(showHudText)) / 2), (y - widgetHeight) - margin, widgetWidth, widgetHeight, showHudText,
                this.mod.config.doesRenderHud(), btn -> this.mod.setHudEnabled(btn.isChecked())));
        this.initLeftWidgets(y, widgetWidth, widgetHeight, margin);
        this.initRightWidgets(y, widgetWidth, widgetHeight, margin);
    }

    private void initLeftWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        for (SpruceOption option : this.leftOptions) {
            this.addDrawableChild(option.createWidget(Position.origin(), widgetWidth));
            y += widgetHeight + margin;
        }
    }

    private void initRightWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        int x = (this.width / 4) * 3 - widgetWidth / 2;
        this.addDrawableChild(new ButtonWidget(x, y, widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.open_color_config"), (button) -> {
            this.mod.config.save();
            if (this.client != null) {
                this.client.setScreen(new KeystrokesColorConfigScreen(this.mod, this));
            }
        }));

        y += widgetHeight + margin;
        for (SpruceOption option : this.rightOptions) {
            if (option != null)
                this.addDrawableChild(option.createWidget(Position.origin(), widgetWidth));
            y += widgetHeight + margin;
        }

        this.addDrawableChild(new SpruceButtonWidget(Position.origin(), widgetWidth, widgetHeight, SpruceTexts.GUI_DONE, (button) -> {
            this.mod.config.save();
            if (this.client != null) {
                this.client.setScreen(null);
            }
        }));
        this.addDrawableChild(this.resetOption.createWidget(Position.origin(), widgetWidth));
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 10, 16777215);
    }
}
