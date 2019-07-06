package com.dogedev.doge.module.modules.player;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventTick;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (isToggled()) {
            mc.rightClickDelayTimer = Math.min(mc.rightClickDelayTimer, 1);
        }
    }
}
