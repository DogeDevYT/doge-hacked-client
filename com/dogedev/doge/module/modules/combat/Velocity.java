package com.dogedev.doge.module.modules.combat;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventReceivePacket;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Keyboard.KEY_NONE, Category.COMBAT);
    }

    @EventTarget
    public void onRecievePacket(EventReceivePacket e) {
        if (isToggled()) {
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
                final S12PacketEntityVelocity vel = (S12PacketEntityVelocity)e.getPacket();
                if (e.getPacket() instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                }
                if (vel.getEntityID() == mc.thePlayer.getEntityId()) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
