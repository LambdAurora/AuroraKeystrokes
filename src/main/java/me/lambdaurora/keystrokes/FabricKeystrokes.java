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

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aperlambda.lambdacommon.utils.LambdaUtils;

import java.awt.*;
import java.util.Timer;


public class FabricKeystrokes implements ClientModInitializer
{
    private static FabricKeystrokes INSTANCE;
    public final   Logger           logger = LogManager.getLogger("FabricKeystrokes");
    public final   KeystrokesConfig config = new KeystrokesConfig(this);
    public         int              cps    = 0;

    @Override
    public void onInitializeClient()
    {
        INSTANCE = this;
        log("Initializing FabricKeystrokes...");
        this.config.load();
        new Timer().scheduleAtFixedRate(LambdaUtils.new_timer_task_from_lambda(() -> this.cps = 0), 0, 1000);
        ClientTickCallback.EVENT.register(client ->
        {
            int times_pressed = ((KeyBindingAccessor) client.options.keyAttack).get_times_pressed();
            if (client.options.keyAttack.isPressed() && times_pressed == 0)
                this.cps++;
        });
    }

    public void log(String info)
    {
        this.logger.info("[FabricKeystrokes] " + info);
    }

    public static FabricKeystrokes get()
    {
        return INSTANCE;
    }

    public static Color parse_color(String hex)
    {
        hex = hex.replace("#", "");
        switch (hex.length()) {
            case 6:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16));
            case 8:
                return new Color(
                        Integer.valueOf(hex.substring(0, 2), 16),
                        Integer.valueOf(hex.substring(2, 4), 16),
                        Integer.valueOf(hex.substring(4, 6), 16),
                        Integer.valueOf(hex.substring(6, 8), 16));
        }
        return null;
    }
}
