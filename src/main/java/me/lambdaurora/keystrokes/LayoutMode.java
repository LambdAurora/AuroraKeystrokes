/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
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
