package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventReceivePacket;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;

public class KeepSprint extends Module {
    public KeepSprint() {
        super("KeepSprint", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onPacket(EventReceivePacket e) {
        try {
            if (e.getPacket() instanceof C0BPacketEntityAction) {
                C0BPacketEntityAction packet = (C0BPacketEntityAction)e.getPacket();
                if (packet.getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
                    e.setCancelled(true);
                }
            }
        } catch (ClassCastException classCastException) {

        }
    }
}
