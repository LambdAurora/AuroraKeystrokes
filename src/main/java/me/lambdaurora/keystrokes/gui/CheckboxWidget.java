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

import java.util.function.Consumer;

/**
 * Represents a checkbox widget.
 */
public class CheckboxWidget extends net.minecraft.client.gui.widget.CheckboxWidget
{
    private final Consumer<CheckboxWidget> on_press;

    public CheckboxWidget(int x, int y, int width, int height, String message, boolean checked, Consumer<CheckboxWidget> on_press)
    {
        super(x, y, width, height, message, checked);
        this.on_press = on_press;
    }

    @Override
    public void onPress()
    {
        super.onPress();
        this.on_press.accept(this);
    }
}
