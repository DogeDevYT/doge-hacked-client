package com.dogedev.doge.module.modules.misc;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import org.lwjgl.input.Keyboard;

public class NoSwing extends Module {
    public NoSwing() {
        super("NoSwing", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!isToggled()) return;

        if (mc.thePlayer.swingProgress <= 0 && !mc.thePlayer.isEating()) {
            mc.thePlayer.swingProgressInt = 5;
        }
    }
}
