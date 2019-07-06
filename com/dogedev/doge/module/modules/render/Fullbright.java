package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {
    private float oldBrightness;

    public Fullbright() {
        super("Fullbright", Keyboard.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        oldBrightness = mc.gameSettings.gammaSetting;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (isToggled()) {
            mc.gameSettings.gammaSetting = 10F;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.gameSettings.gammaSetting = oldBrightness;
    }
}
