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
import me.lambdaurora.keystrokes.TextDisplayMode;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.FatalErrorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
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
    private       ButtonListWidget list;

    public KeystrokesConfigScreen(@NotNull AuroraKeystrokes mod)
    {
        super(new LiteralText("Fabric Keystrokes Config"));
        this.mod = mod;
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
        this.list = new ButtonListWidget(this.minecraft, this.width, this.height - 32, 32, this.height - 32, 25);
        int widget_width = 204;
        int widget_height = 20;
        int margin = 4;
        int y = this.height / 4 + 24 + -16;
        String show_hud_text = I18n.translate("keystrokes.menu.show_hud");
        this.addButton(new CheckboxWidget((this.width / 2 - (24 + this.font.getStringWidth(show_hud_text)) / 2), (y - widget_height) - margin, widget_width, widget_height, show_hud_text,
                this.mod.config.does_render_hud(), btn -> this.mod.config.set_render_hud(btn.isChecked())));
        /*this.list.addSingleOptionEntry(new BooleanOption(show_hud_text, game_options -> this.mod.config.does_render_hud(),
                (game_options, value) -> this.mod.config.set_render_hud(value)));
        this.list.addOptionEntry(
                new DoubleOption("X", 0.0, 100.0, 0.5f,
                        game_options -> this.mod.config.get_x(),
                        (game_options, value) -> this.mod.config.set_x(value),
                        (game_options, option) -> "X: " + option.get(game_options)),
                new DoubleOption("Y", 0.0, 100.0, 0.5f,
                        game_options -> this.mod.config.get_y(),
                        (game_options, value) -> this.mod.config.set_y(value),
                        (game_options, option) -> "Y: " + option.get(game_options)));
        this.list.addSingleOptionEntry(new DoubleOption("keystrokes.menu.padding", 0.0, 20.0, 1.0f,
                game_options -> (double) this.mod.config.get_padding(),
                (game_options, value) -> this.mod.config.set_padding(value.intValue()),
                (game_options, option) -> I18n.translate("keystrokes.menu.padding") + ": " + option.get(game_options)));
        this.list.addSingleOptionEntry(new BooleanOption("keystrokes.menu.show_movement_boxes", game_options -> this.mod.config.show_movement_boxes(),
                (game_options, value) -> this.mod.config.set_movement_boxes(value)));
        this.list.addOptionEntry(
                new BooleanOption("keystrokes.menu.show_attack_box", game_options -> this.mod.config.show_attack_box(),
                        (game_options, value) -> this.mod.config.set_attack_box(value)),
                new BooleanOption("keystrokes.menu.show_use_box", game_options -> this.mod.config.show_use_box(),
                        (game_options, value) -> this.mod.config.set_use_box(value)));
        this.list.addOptionEntry(
                new BooleanOption("keystrokes.menu.show_sneak_box", game_options -> this.mod.config.show_sneak_box(),
                        (game_options, value) -> this.mod.config.set_sneak_box(value)),
                new BooleanOption("keystrokes.menu.show_jump_box", game_options -> this.mod.config.show_jump_box(),
                        (game_options, value) -> this.mod.config.set_jump_box(value)));
        this.list.addOptionEntry(
                new BooleanOption("keystrokes.menu.show_cps", game_options -> this.mod.config.show_cps(),
                        (game_options, value) -> this.mod.config.set_show_cps(value)),
                new BooleanOption("keystrokes.menu.attach_cps", game_options -> this.mod.config.attached_cps(),
                        (game_options, value) -> this.mod.config.set_attached_cps(value)));*/
        this.init_left_widgets(y, widget_width, widget_height, margin);
        this.init_right_widgets(y, widget_width, widget_height, margin);
        /*this.children.add(this.list);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, I18n.translate("gui.done"), (button) -> {
            this.mod.config.save();
            this.minecraft.openScreen(null);
            this.minecraft.mouse.lockCursor();
        }));*/
    }

    private void init_left_widgets(int y, int widget_width, int widget_height, int margin)
    {
        int x = this.width / 4 - widget_width / 2;
        this.addButton(new KeystrokesSliderWidget(x, y, widget_width, widget_height, "X",
                this.mod.config.get_x() / 100.0, btn -> this.mod.config.set_x(btn.get_value() * 100.0)));
        this.addButton(new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, "Y",
                this.mod.config.get_y() / 100.0, btn -> this.mod.config.set_y(btn.get_value() * 100.0)));
        this.addButton(new KeystrokesSliderWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.padding"),
                this.mod.config.get_padding() / 20.0, btn -> this.mod.config.set_padding((int) (btn.get_value() * 20.0))));
        this.addButton(new CheckboxWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.show_movement_boxes"),
                this.mod.config.show_movement_boxes(), btn -> this.mod.config.set_movement_boxes(btn.isChecked())));
        this.addButton(new CheckboxWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.show_attack_box"),
                this.mod.config.show_attack_box(), btn -> this.mod.config.set_attack_box(btn.isChecked())));
        this.addButton(new CheckboxWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.show_use_box"),
                this.mod.config.show_use_box(), btn -> this.mod.config.set_use_box(btn.isChecked())));
        this.addButton(new CheckboxWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.show_sneak_box"),
                this.mod.config.show_sneak_box(), btn -> this.mod.config.set_sneak_box(btn.isChecked())));
        this.addButton(new CheckboxWidget(x, y + (widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.show_jump_box"),
                this.mod.config.show_jump_box(), btn -> this.mod.config.set_jump_box(btn.isChecked())));
    }

    private void init_right_widgets(int y, int widget_width, int widget_height, int margin)
    {
        int x = (this.width / 4) * 3 - widget_width / 2;
        this.addButton(new ButtonWidget(x, y, widget_width, widget_height, I18n.translate("keystrokes.menu.open_color_config"), (button) -> {
            this.mod.config.save();
            this.minecraft.openScreen(new KeystrokesColorConfigScreen(this.mod, this));
        }));
        this.addButton(new ButtonWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.text_display_mode") + ": " + this.mod.config.get_text_display_mode().get_translated_name(), btn -> {
            TextDisplayMode next = this.mod.config.get_text_display_mode().next();
            btn.setMessage(I18n.translate("keystrokes.menu.text_display_mode") + ": " + next.get_translated_name());
            this.mod.config.set_text_display_mode(next);
        }));
        this.addButton(new CheckboxWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.show_cps"), this.mod.config.show_cps(), btn -> this.mod.config.set_show_cps(btn.isChecked())));
        this.addButton(new CheckboxWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("keystrokes.menu.attach_cps"), this.mod.config.attached_cps(), btn -> this.mod.config.set_attached_cps(btn.isChecked())));
        this.addButton(new ButtonWidget(x, (y += widget_height + margin), widget_width, widget_height, I18n.translate("gui.done"), (button) -> {
            this.mod.config.save();
            this.minecraft.openScreen(null);
            this.minecraft.mouse.lockCursor();
        }));
        this.addButton(new ButtonWidget(x, y + (widget_height + margin) * 2, widget_width, widget_height, I18n.translate("keystrokes.menu.reset"), btn ->
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
        //this.list.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 10, 16777215);

        super.render(mouseX, mouseY, delta);
    }
}
