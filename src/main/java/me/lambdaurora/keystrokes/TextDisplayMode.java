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

package me.lambdaurora.keystrokes;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents the display modes of the text inside the keystrokes boxes.
 */
public enum TextDisplayMode
{
    ACTION_NAME(key_binding -> I18n.translate(key_binding.getId())),
    KEY_NAME(KeyBinding::getLocalizedName);

    private final Function<KeyBinding, String> name_mapper;

    TextDisplayMode(@NotNull Function<KeyBinding, String> name_mapper)
    {
        this.name_mapper = name_mapper;
    }

    /**
     * Gets the text from the specified key binding.
     *
     * @param key_binding The specified key binding.
     * @return The text describing the key binding.
     */
    public String get_text(KeyBinding key_binding)
    {
        return this.name_mapper.apply(key_binding);
    }

    /**
     * Gets the text display mode from a string identifier.
     *
     * @param id The string identifier.
     * @return The optional text display mode.
     */
    public static Optional<TextDisplayMode> by_id(String id)
    {
        return Arrays.stream(values()).filter(tdm -> tdm.name().equalsIgnoreCase(id)).findFirst();
    }
}
