/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.aperlambda.lambdacommon.utils.Nameable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents the display modes of the text inside the keystrokes boxes.
 */
public enum TextDisplayMode implements Nameable
{
    ACTION_NAME(keyBinding -> new TranslatableText(keyBinding.getTranslationKey())),
    KEY_NAME(KeyBinding::getBoundKeyLocalizedText);

    private final Function<KeyBinding, Text> nameMapper;

    TextDisplayMode(@NotNull Function<KeyBinding, Text> nameMapper)
    {
        this.nameMapper = nameMapper;
    }

    /**
     * Returns the next display mode available.
     *
     * @return The next available display mode.
     */
    public TextDisplayMode next()
    {
        TextDisplayMode[] v = values();
        if (v.length == this.ordinal() + 1)
            return v[0];
        return v[this.ordinal() + 1];
    }

    /**
     * Gets the translated name of this text display mode.
     *
     * @return The translated name of this text display mode.
     */
    public TranslatableText getTranslatedName()
    {
        return new TranslatableText("keystrokes.text_display_mode." + this.getName());
    }

    /**
     * Gets the text from the specified key binding.
     *
     * @param keyBinding The specified key binding.
     * @return The text describing the key binding.
     */
    public Text getText(KeyBinding keyBinding)
    {
        return this.nameMapper.apply(keyBinding);
    }

    /**
     * Gets the text display mode from a string identifier.
     *
     * @param id The string identifier.
     * @return The optional text display mode.
     */
    public static Optional<TextDisplayMode> byId(String id)
    {
        return Arrays.stream(values()).filter(tdm -> tdm.getName().equalsIgnoreCase(id)).findFirst();
    }

    @Override
    public @NotNull String getName()
    {
        return this.name().toLowerCase();
    }
}
