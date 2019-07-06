package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (isToggled()) {
            if (!mc.thePlayer.isCollidedHorizontally && mc.thePlayer.moveForward > 0) {
                mc.thePlayer.setSprinting(true);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.thePlayer.setSprinting(false);
    }
}
