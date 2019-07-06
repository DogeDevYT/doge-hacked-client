package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Step extends Module {
    private int ticks;

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Basic");
        options.add("AAC 3.5.0");
        Doge.instance.settingsManager.rSetting(new Setting("Step Mode", this, "Basic", options));
    }

    public Step() {
        super("Step", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        ticks = 0;
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        String mode = Doge.instance.settingsManager.getSettingByName("Step Mode").getValString();
        if (isToggled()) {
            this.setDisplayName("Step" + "[" + mode + "]");
            if (mode.equalsIgnoreCase("Basic")) {
                mc.thePlayer.stepHeight = 1.5F;
            }
            if (mode.equalsIgnoreCase("AAC 3.5.0")) {
                if (mc.thePlayer.isCollidedHorizontally) {
                    switch (ticks) {
                        case 0:
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.jump();
                            }
                            break;
                        case 7:
                            mc.thePlayer.motionY = 0;
                            break;
                        case 8:
                            if (!mc.thePlayer.onGround) {
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ);
                            }
                            break;
                    }
                    ticks++;
                } else {
                    ticks = 0;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        String mode = Doge.instance.settingsManager.getSettingByName("Step Mode").getValString();
        super.onDisable();

        if (mode.equalsIgnoreCase("Basic")) {
            mc.thePlayer.stepHeight = 0.5F;
        }
    }
}
