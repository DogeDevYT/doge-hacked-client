package com.dogedev.doge.module.modules.combat;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class FastBow extends Module
{
    public FastBow() {
        super("FastBow", Keyboard.KEY_NONE, Category.COMBAT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!isToggled()) return;
        
        if (mc.thePlayer.onGround && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.pressed) {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 7199);
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.2, mc.thePlayer.posZ, mc.thePlayer.onGround));
                for (int i = 0; i < 20; ++i) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-9, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                }
            }
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
        }
    }
    
    public void setSpeed(final double speed) {
        mc.thePlayer.motionX = -MathHelper.sin(this.getDirection()) * speed;
        mc.thePlayer.motionZ = MathHelper.cos(this.getDirection()) * speed;
    }
    
    public float getDirection() {
        float yaw = mc.thePlayer.rotationYawHead;
        final float forward = mc.thePlayer.moveForward;
        final float strafe = mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }
}
