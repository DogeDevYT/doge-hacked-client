package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventCollide;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.PlayerUtils;
import org.lwjgl.input.Keyboard;

public class Phase extends Module {
    public Phase() {
        super("Phase", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    private int reset;
    private double dist = 1D;

    @EventTarget
    public void onUpdate(EventUpdate event) {
        reset -= 1;
        double xOff = 0;
        double zOff = 0;
        double multi = 2.6D;
        double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
        double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90F));
        xOff = mc.thePlayer.moveForward * 2.6D * mx + mc.thePlayer.moveStrafing * 2.6D * mz;
        zOff = mc.thePlayer.moveForward * 2.6D * mz + mc.thePlayer.moveStrafing * 2.6D * mx;
        if(PlayerUtils.isInsideBlock() && mc.thePlayer.isSneaking())
            reset = 1;
        if(reset > 0)
            mc.thePlayer.boundingBox.offsetAndUpdate(xOff, 0, zOff);
    }

    @EventTarget
    public boolean onCollision(EventCollide event) {
        if((PlayerUtils.isInsideBlock() && mc.gameSettings.keyBindJump.isKeyDown()) || (!(PlayerUtils.isInsideBlock()) && event.getBoundingBox() != null && event.getBoundingBox().maxY > mc.thePlayer.boundingBox.minY && mc.thePlayer.isSneaking()))
            event.setBoundingBox(null);
        return true;
    }
}
