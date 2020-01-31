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
import me.lambdaurora.spruceui.SpruceButtonWidget;
import me.lambdaurora.spruceui.SpruceCheckboxWidget;
import me.lambdaurora.spruceui.Tooltip;
import me.lambdaurora.spruceui.option.SpruceCyclingOption;
import me.lambdaurora.spruceui.option.SpruceDoubleOption;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
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

    public KeystrokesConfigScreen(@NotNull AuroraKeystrokes mod)
    {
        super(new LiteralText("AuroraKeystrokes Config"));
        this.mod = mod;

        this.xOption = new SpruceDoubleOption("keystrokes.menu.x", 0.0, 100.0, 0.5F,
                this.mod.config::getX,
                this.mod.config::setX,
                option -> "X: " + option.get(),
                new TranslatableText("keystrokes.tooltip.x"));
        this.yOption = new SpruceDoubleOption("keystrokes.menu.y", 0.0, 100.0, 0.5F,
                this.mod.config::getY,
                this.mod.config::setY,
                (option) -> "Y: " + option.get(),
                new TranslatableText("keystrokes.tooltip.y"));
        this.paddingOption = new SpruceDoubleOption("keystrokes.menu.padding", 0.0, 20.0, 1.0F,
                () -> (double) this.mod.config.getPadding(),
                newValue -> this.mod.config.setPadding(newValue.intValue()),
                (option) -> option.getDisplayPrefix() + option.get(),
                new TranslatableText("keystrokes.tooltip.padding"));
        this.textDisplayModeOption = new SpruceCyclingOption("keystrokes.menu.text_display_mode",
                amount -> this.mod.config.setTextDisplayMode(this.mod.config.getTextDisplayMode().next()),
                option -> option.getDisplayPrefix() + this.mod.config.getTextDisplayMode().getTranslatedName(),
                new TranslatableText("keystrokes.tooltip.text_display_mode"));
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
        String showHudText = I18n.translate("keystrokes.menu.show_hud");
        this.addButton(new SpruceCheckboxWidget((this.width / 2 - (24 + this.font.getStringWidth(showHudText)) / 2), (y - widgetHeight) - margin, widgetWidth, widgetHeight, showHudText,
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
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.show_movement_boxes"),
                this.mod.config.showMovementBoxes(), btn -> this.mod.config.setMovementBoxes(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.show_attack_box"),
                this.mod.config.showAttackBox(), btn -> this.mod.config.setAttackBox(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.show_use_box"),
                this.mod.config.showUseBox(), btn -> this.mod.config.setUseBox(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.show_sneak_box"),
                this.mod.config.showSneakBox(), btn -> this.mod.config.setSneakBox(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, y + (widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.show_jump_box"),
                this.mod.config.showJumpBox(), btn -> this.mod.config.setJumpBox(btn.isChecked())));
    }

    private void initRightWidgets(int y, int widgetWidth, int widgetHeight, int margin)
    {
        int x = (this.width / 4) * 3 - widgetWidth / 2;
        this.addButton(new ButtonWidget(x, y, widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.open_color_config"), (button) -> {
            this.mod.config.save();
            this.minecraft.openScreen(new KeystrokesColorConfigScreen(this.mod, this));
        }));
        this.addButton(this.textDisplayModeOption.createButton(null, x, (y += widgetHeight + margin), widgetWidth));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.show_cps"), this.mod.config.showCps(), btn -> this.mod.config.setShowCps(btn.isChecked())));
        this.addButton(new SpruceCheckboxWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.attach_cps"), this.mod.config.attachedCps(), btn -> this.mod.config.setAttachedCps(btn.isChecked())));
        this.addButton(new SpruceButtonWidget(x, (y += widgetHeight + margin), widgetWidth, widgetHeight, I18n.translate("gui.done"), (button) -> {
            this.mod.config.save();
            this.minecraft.openScreen(null);
            this.minecraft.mouse.lockCursor();
        }));
        this.addButton(new SpruceButtonWidget(x, y + (widgetHeight + margin) * 2, widgetWidth, widgetHeight, I18n.translate("keystrokes.menu.reset"), btn ->
                this.minecraft.openScreen(new ConfirmScreen(confirm -> {
                    if (confirm) {
                        if (new File("config/keystrokes.toml").delete()) {
                            this.mod.config.load();
                            this.minecraft.openScreen(new KeystrokesConfigScreen(this.mod));
                        } else {
                            this.minecraft.openScreen(new FatalErrorScreen(new TranslatableText("keystrokes.menu.reset"), I18n.translate("keystrokes.error.cannot_reset")));
                        }
                    } else this.minecraft.openScreen(this);
                }, new TranslatableText("keystrokes.menu.reset"), new TranslatableText("keystrokes.menu.confirm_reset")))));
    }

    public void render(int mouseX, int mouseY, float delta)
    {
        this.renderBackground();

        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 10, 16777215);

        super.render(mouseX, mouseY, delta);

        Tooltip.renderAll();
    }
}
