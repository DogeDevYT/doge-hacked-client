package com.dogedev.doge.module.modules.misc;

import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.module.ModuleManager;
import org.lwjgl.input.Keyboard;

public class Panic extends Module {
    public Panic() {
        super("Panic", Keyboard.KEY_P, Category.MISC);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        for (Module m : ModuleManager.getModules()) {
            if (m.isToggled()) {
                m.toggle();
            }
        }
    }
}
