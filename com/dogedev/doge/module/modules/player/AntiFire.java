package com.dogedev.doge.module.modules.player;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class AntiFire extends Module {
    public AntiFire() {
        super("AntiFire", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!isToggled()) return;

        if(!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.onGround && mc.thePlayer.isBurning()) {
            for(int i = 0; i < 100; i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
    }
}
