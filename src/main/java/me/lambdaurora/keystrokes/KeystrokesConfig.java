/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of AuroraKeystrokes.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.keystrokes;

import com.electronwill.nightconfig.core.file.FileConfig;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Represents the AuroraKeytrokes configuration.
 */
public class KeystrokesConfig
{
    private final FileConfig       config          = FileConfig.builder("config/keystrokes.toml").concurrent().defaultResource("/config.toml").build();
    private final AuroraKeystrokes mod;
    private       TextDisplayMode  textDisplayMode = TextDisplayMode.ACTION_NAME;

    public KeystrokesConfig(@NotNull AuroraKeystrokes mod)
    {
        this.mod = mod;
    }

    public void load()
    {
        this.config.load();
        this.textDisplayMode = TextDisplayMode.byId(this.config.getOrElse("hud.text_display_mode", "action_name")).orElseGet(() -> {
            this.mod.log("Could not load `text_display_mode` property: invalid value.");
            return TextDisplayMode.ACTION_NAME;
        });
    }

    public void save()
    {
        this.config.save();
    }

    public boolean doesRenderHud()
    {
        return this.config.getOrElse("hud.enable", true);
    }

    public void setRenderHud(boolean renderHud)
    {
        this.config.set("hud.enable", renderHud);
    }

    public double getX()
    {
        return this.config.getOrElse("hud.x", 10.0);
    }

    public void setX(double x)
    {
        this.config.set("hud.x", x);
    }

    public double getY()
    {
        return this.config.getOrElse("hud.y", 10.0);
    }

    public void setY(double y)
    {
        this.config.set("hud.y", y);
    }

    public int getPadding()
    {
        return this.config.getOrElse("hud.padding", 4);
    }

    public void setPadding(int padding)
    {
        this.config.set("hud.padding", padding);
    }

    public boolean showMovementBoxes()
    {
        return this.config.getOrElse("hud.show_movement_boxes", true);
    }

    public void setMovementBoxes(boolean show)
    {
        this.config.set("hud.show_movement_boxes", show);
    }

    public boolean showAttackBox()
    {
        return this.config.getOrElse("hud.show_attack_box", true);
    }

    public void setAttackBox(boolean show)
    {
        this.config.set("hud.show_attack_box", show);
    }

    public boolean showUseBox()
    {
        return this.config.getOrElse("hud.show_use_box", true);
    }

    public void setUseBox(boolean show)
    {
        this.config.set("hud.show_use_box", show);
    }

    public boolean showSneakBox()
    {
        return this.config.getOrElse("hud.show_sneak_box", true);
    }

    public void setSneakBox(boolean show)
    {
        this.config.set("hud.show_sneak_box", show);
    }

    public boolean showJumpBox()
    {
        return this.config.getOrElse("hud.show_jump_box", true);
    }

    public void setJumpBox(boolean show)
    {
        this.config.set("hud.show_jump_box", show);
    }

    public TextDisplayMode getTextDisplayMode()
    {
        return this.textDisplayMode;
    }

    public void setTextDisplayMode(TextDisplayMode textDisplayMode)
    {
        if (textDisplayMode != this.textDisplayMode) {
            this.textDisplayMode = textDisplayMode;
            this.config.set("hud.text_display_mode", textDisplayMode.name().toLowerCase());
        }
    }

    public boolean showCps()
    {
        return this.config.getOrElse("cps.show", true);
    }

    public void setShowCps(boolean show)
    {
        this.config.set("cps.show", show);
    }

    public boolean attachedCps()
    {
        return this.config.getOrElse("cps.attached", true);
    }

    public void setAttachedCps(boolean attached)
    {
        this.config.set("cps.attached", attached);
    }

    public LayoutMode getLayout()
    {
        return LayoutMode.fromName(this.config.getOrElse("hud.layout", "cross"));
    }

    public void setLayout(LayoutMode layout)
    {
        this.config.set("hud.layout", layout.getName());
    }

    private String hex(int i)
    {
        String res = Integer.toHexString(i);
        if (res.length() == 1)
            return "0" + res;
        return res;
    }

    public Color getColorNormal()
    {
        return AuroraKeystrokes.parseColor(this.config.getOrElse("colors.normal", "#FFFFFFFF"));
    }

    public void setColorNormal(@NotNull Color color)
    {
        this.config.set("colors.normal", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public Color getColorPressed()
    {
        return AuroraKeystrokes.parseColor(this.config.getOrElse("colors.pressed", "#FFA000FF"));
    }

    public void setColorPressed(@NotNull Color color)
    {
        this.config.set("colors.pressed", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public Color getBackgroundNormal()
    {
        return AuroraKeystrokes.parseColor(this.config.getOrElse("colors.background_normal", "#000000AA"));
    }

    public void setBackgroundNormal(@NotNull Color color)
    {
        this.config.set("colors.background_normal", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public Color getBackgroundPressed()
    {
        return AuroraKeystrokes.parseColor(this.config.getOrElse("colors.background_pressed", "#FFFFFFAA"));
    }

    public void setBackgroundPressed(@NotNull Color color)
    {
        this.config.set("colors.background_pressed", "#" + hex(color.getRed()) + hex(color.getGreen()) + hex(color.getBlue()) + hex(color.getAlpha()));
    }

    public boolean useRainbowText()
    {
        return this.config.getOrElse("colors.rainbow", false);
    }

    public void setRainbowText(boolean rainbow)
    {
        this.config.set("colors.rainbow", rainbow);
    }

    public double getRainbowSaturation()
    {
        return this.config.getOrElse("colors.rainbow_saturation", 0.8D);
    }

    public void setRainbowSaturation(double saturation)
    {
        this.config.set("colors.rainbow_saturation", saturation);
    }
}
