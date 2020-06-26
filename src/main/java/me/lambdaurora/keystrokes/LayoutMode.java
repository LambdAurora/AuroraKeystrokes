/*
 * AuroraKeystrokes
 * Copyright (C) 2020  LambdAurora
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

package me.lambdaurora.keystrokes;

import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

public enum LayoutMode implements Nameable
{
    CROSS("cross"),
    PYRAMID("pyramid");

    private String name;

    LayoutMode(String name)
    {
        this.name = name;
    }

    /**
     * Returns the layout mode from its name.
     * @param name The layout name.
     * @return The layout.
     */
    public static LayoutMode fromName(@NotNull String name)
    {
        for (LayoutMode layout : LayoutMode.values()) {
            if (layout.getName().equals(name))
                return layout;
        }
        return CROSS;
    }

    /**
     * Returns the next layout mode available.
     *
     * @return The next layout display mode.
     */
    public LayoutMode next()
    {
        LayoutMode[] v = values();
        if (v.length == this.ordinal() + 1)
            return v[0];
        return v[this.ordinal() + 1];
    }

    @Override
    public @NotNull String getName()
    {
        return this.name;
    }
}
