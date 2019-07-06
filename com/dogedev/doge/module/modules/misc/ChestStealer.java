package com.dogedev.doge.module.modules.misc;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.NumberUtils;
import com.dogedev.doge.utils.TimeHelper;
import net.minecraft.inventory.ContainerChest;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    private TimeHelper timeHelper = new TimeHelper();

    public ChestStealer() {
        super("ChestStealer", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!isToggled()) return;

        if ((mc.thePlayer.openContainer != null) && (mc.thePlayer.openContainer instanceof ContainerChest)) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                if ((chest.getLowerChestInventory().getStackInSlot(i) != null) && timeHelper.hasReached(NumberUtils.random(150, 200))) {
                    mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                    timeHelper.reset();
                }
            }
            if (chest.getInventory().isEmpty()) {
                mc.displayGuiScreen(null);
            }
        }
    }
}
