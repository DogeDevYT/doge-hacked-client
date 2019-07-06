package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.BlockUtils;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Jesus extends Module {
    private TimeHelper timer = new TimeHelper();

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Basic");
        Doge.instance.settingsManager.rSetting(new Setting("Jesus Mode", this, "Basic", options));
    }

    public Jesus() {
        super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Doge.instance.settingsManager.getSettingByName("Jesus Mode").getValString();
        if (isToggled()) {
            this.setDisplayName("Jesus" + "[" + mode + "]");
            if (BlockUtils.isInLiquid() && !mc.thePlayer.isSneaking()) {
                if (mode.equalsIgnoreCase("Basic")) {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.jumpMovementFactor = 1.12f;
                }
            }
        }
    }
}
