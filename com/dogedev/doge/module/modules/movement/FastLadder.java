package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class FastLadder extends Module {
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("AAC 3.3.10");
        Doge.instance.settingsManager.rSetting(new Setting("FastLadder Mode", this, "AAC 3.3.10", options));
    }

    public FastLadder() {
        super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        String mode = Doge.instance.settingsManager.getSettingByName("FastLadder Mode").getValString();
        if (mode.equalsIgnoreCase("AAC 3.3.10")) {
            if (mc.thePlayer.isOnLadder()) {
                mc.thePlayer.motionY = 0.169;
            }
        }
    }
}
