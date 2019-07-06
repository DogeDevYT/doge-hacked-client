package com.dogedev.doge.module.modules.player;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class Zoot extends Module {
    public Zoot() {
        super("Zoot", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!isToggled()) return;

        if (mc.thePlayer.onGround && !mc.thePlayer.isSwingInProgress) {
            Potion[] potionTypes;
            for (int length = (potionTypes = Potion.potionTypes).length, i = 0; i < length; ++i) {
                final Potion potion = potionTypes[i];
                if (potion != null) {
                    final PotionEffect effect = mc.thePlayer.getActivePotionEffect(potion);
                    if (effect != null && potion.isBadEffect()) {
                        for (int j = 0; j < effect.getDuration() / 20; ++j) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        }
                    }
                }
            }
            if (mc.thePlayer.isBurning() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()) {
                for (int k = 0; k < 20; ++k) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                }
            }
        }
    }
}
