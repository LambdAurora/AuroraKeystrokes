/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes.gui;

import me.lambdaurora.keystrokes.AuroraKeystrokes;
import me.lambdaurora.spruceui.SpruceButtonWidget;
import me.lambdaurora.spruceui.SpruceCheckboxWidget;
import me.lambdaurora.spruceui.SpruceTexts;
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.option.SpruceCyclingOption;
import me.lambdaurora.spruceui.option.SpruceDoubleOption;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents the config screen of AuroraKeystrokes.
 */
public class KeystrokesConfigScreen extends Screen
{
    private final AuroraKeystrokes mod;

    private final Option xOption;
    private final Option yOption;
    private final Option paddingOption;
    private final Option textDisplayModeOption;
    private final Option layoutOption;

    public KeystrokesConfigScreen(@NotNull AuroraKeystrokes mod)
    {
        super(new TranslatableText("keystrokes.menu.title.main"));
        this.mod = mod;

        this.xOption = new SpruceDoubleOption("keystrokes.menu.x", 0.0, 100.0, 0.5F,
                this.mod.config::getX,
                this.mod.config::setX,
                option -> new LiteralText("X: " + option.get()),
                new TranslatableText("keystrokes.tooltip.x"));
        this.yOption = new SpruceDoubleOption("keystrokes.menu.y", 0.0, 100.0, 0.5F,
                this.mod.config::getY,
                this.mod.config::setY,
                (option) -> new LiteralText("Y: " + option.get()),
                new TranslatableText("keystrokes.tooltip.y"));
        this.paddingOption = new SpruceDoubleOption("keystrokes.menu.padding", 0.0, 20.0, 1.0F,
                () -> (double) this.mod.config.getPadding(),
                newValue -> this.mod.config.setPadding(newValue.intValue()),
                (option) -> option.getDisplayPrefix().append(String.valueOf(option.get())),
                new TranslatableText("keystrokes.tooltip.padding"));
        this.textDisplayModeOption = new SpruceCyclingOption("keystrokes.menu.text_display_mode",
                amount -> this.mod.config.setTextDisplayMode(this.mod.config.getTextDisplayMode().next()),
                option -> option.getDisplayPrefix().append(this.mod.config.getTextDisplayMode().getTranslatedName()),
                new TranslatableText("keystrokes.tooltip.text_display_mode"));
        this.layoutOption = new SpruceCyclingOption("keystrokes.menu.layout",
                amount -> this.mod.config.setLayout(this.mod.config.getLayout().next()),
                option -> option.getDisplayPrefix().append(new TranslatableText("keystrokes.layout." + this.mod.config.getLayout().getName())),
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
        int y = this.height / 4 + 24 + -32;
        TranslatableText showHudText = new TranslatableText("keystrokes.menu.show_hud");
        this.addButton(new SpruceCheckboxWidget((this.width / 2 - (24 + this.textRenderer.getWidth(showHudText)) / 2), (y - widgetHeight) - margin, widgetWidth, widgetHeight, showHudText,
                this.mod.config.doesRenderHud(), btn -> this.mod.setHudEnabled(btn.isChecked())));
        this.initLeftWidgets(y, widgetWidth, widgetHeight, margin);
        this.initRightWidgets(y, widgetWidth, widgetHeight, margin);
    }

    private void initLeftWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        int x = this.width / 4 - widgetWidth / 2;
        this.addButton(this.xOption.createButton(null, x, y, widgetWidth));
        this.addButton(this.yOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(this.paddingOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.show_movement_boxes"),
                this.mod.config.showMovementBoxes(), btn -> this.mod.config.setMovementBoxes(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.show_attack_box"),
                this.mod.config.showAttackBox(), btn -> this.mod.config.setAttackBox(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.show_use_box"),
                this.mod.config.showUseBox(), btn -> this.mod.config.setUseBox(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.show_sneak_box"),
                this.mod.config.showSneakBox(), btn -> this.mod.config.setSneakBox(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, y + (widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.show_jump_box"),
                this.mod.config.showJumpBox(), btn -> this.mod.config.setJumpBox(btn.isChecked())));
    }

    private void initRightWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        int x = (this.width / 4) * 3 - widgetWidth / 2;
        this.addButton(new ButtonWidget(x, y, widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.open_color_config"), (button) -> {
            this.mod.config.save();
            this.client.openScreen(new KeystrokesColorConfigScreen(this.mod, this));
        }));
        this.addButton(this.textDisplayModeOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.show_cps"), this.mod.config.showCps(), btn -> this.mod.config.setShowCps(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, new TranslatableText("keystrokes.menu.attach_cps"), this.mod.config.attachedCps(), btn -> this.mod.config.setAttachedCps(btn.isChecked())));
        this.addButton(this.layoutOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(new SpruceButtonWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, SpruceTexts.GUI_DONE, (button) -> {
            this.mod.config.save();
            this.client.openScreen(null);
            this.client.mouse.lockCursor();
        }));
        this.addButton(new SpruceButtonWidget(x, y + (widgetHeight + margin) * 2, widgetWidth, widgetHeight, SpruceTexts.RESET_TEXT, btn ->
                this.client.openScreen(new ConfirmScreen(confirm -> {
                    if (confirm) {
                        if (new File("config/keystrokes.toml").delete()) {
                            this.mod.config.load();
                            this.client.openScreen(new KeystrokesConfigScreen(this.mod));
                        } else {
                            this.client.openScreen(new FatalErrorScreen(SpruceTexts.RESET_TEXT, new TranslatableText("keystrokes.error.cannot_reset")));
                        }
                    } else this.client.openScreen(this);
                }, SpruceTexts.RESET_TEXT, new TranslatableText("keystrokes.menu.confirm_reset")))));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(matrices);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 10, 16777215);

        super.render(matrices, mouseX, mouseY, delta);

        Tooltip.renderAll(matrices);
    }
}
