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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.NotNull;

public class KeystrokesConfigScreen extends Screen
{
    private final FabricKeystrokes mod;

    public KeystrokesConfigScreen(@NotNull FabricKeystrokes mod)
    {
        super(new LiteralText("Keystrokes Config"));
        this.mod = mod;
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    protected void init()
    {
        super.init();
        this.mod.config.load();
        this.addButton(new ButtonWidget(this.width / 2 - 102, (this.height / 4) * 3 + 24 + -16, 204, 20, I18n.translate("gui.done"), (button) -> {
            this.mod.config.save();
            this.minecraft.openScreen(null);
            this.minecraft.mouse.lockCursor();
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, "Show hud: " + this.mod.config.does_render_hud(), (button) -> {
            boolean new_state = !this.mod.config.does_render_hud();
            this.mod.config.set_render_hud(new_state);
            button.setMessage("Show hud: " + new_state);
        }));
    }

    public void render(int mouseX, int mouseY, float delta)
    {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 10, 16777215);

        super.render(mouseX, mouseY, delta);
    }
}
