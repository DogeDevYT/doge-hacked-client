package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.Doge;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class HUD extends Module {
    @Override
    public void setup() {
        Doge.instance.settingsManager.rSetting(new Setting("FPSCounter", this, true));
    }

    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER);
    }
}
