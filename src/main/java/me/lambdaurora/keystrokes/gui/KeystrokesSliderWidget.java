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

import net.minecraft.client.gui.widget.SliderWidget;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a slightly modified slider widget.
 */
public class KeystrokesSliderWidget extends SliderWidget
{
    private       String                           base_message;
    private final Consumer<KeystrokesSliderWidget> apply_consumer;
    private       double                           multiplier;
    private       String                           sign;

    protected KeystrokesSliderWidget(int x, int y, int width, int height, @NotNull String message, double progress, @NotNull Consumer<KeystrokesSliderWidget> apply_consumer, double multiplier, String sign)
    {
        super(x, y, width, height, progress);
        this.base_message = message;
        this.apply_consumer = apply_consumer;
        this.multiplier = multiplier;
        this.sign = sign;
        this.updateMessage();
    }

    protected KeystrokesSliderWidget(int x, int y, int width, int height, @NotNull String message, double progress, @NotNull Consumer<KeystrokesSliderWidget> apply_consumer)
    {
        this(x, y, width, height, message, progress, apply_consumer, 100.0, "%");
    }

    /**
     * Gets the value of the slider.
     *
     * @return The value of the slider.
     */
    public double get_value()
    {
        return this.value;
    }

    /**
     * Returns the value of this slider as an integer .
     *
     * @return The value as an integer.
     */
    public int get_int_value()
    {
        return (int) (this.value * this.multiplier);
    }

    /**
     * Sets the value of this slider.
     *
     * @param value The new value as an integer.
     */
    public void set_int_value(int value)
    {
        this.value = value / this.multiplier;
        this.updateMessage();
    }

    /**
     * Gets the base message of the slider.
     *
     * @return The base message of the slider.
     */
    public @NotNull String get_base_message()
    {
        return base_message;
    }

    /**
     * Sets the base message of the slider.
     *
     * @param base_message The base message of the slider.
     */
    public void set_base_message(@NotNull String base_message)
    {
        this.base_message = base_message;
    }

    @Override
    protected void updateMessage()
    {
        this.setMessage(this.base_message + ": " + this.get_int_value() + sign);
    }

    @Override
    protected void applyValue()
    {
        this.apply_consumer.accept(this);
    }
}
