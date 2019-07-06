package com.dogedev.doge.module.modules.combat;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventSendPacket;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class MoreKB extends Module {
    public MoreKB() {
        super("MoreKB", Keyboard.KEY_NONE, Category.COMBAT);
    }

    @EventTarget
    public void onEvent(EventSendPacket e) {
        C02PacketUseEntity packet;
        if (e.getPacket() instanceof C02PacketUseEntity && (packet = (C02PacketUseEntity)e.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && this.mc.thePlayer.onGround) {
            mc.thePlayer.setSprinting(true);
            for (int i = 0; i < 150; ++i) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(this.mc.thePlayer.onGround));
            }
        }
    }
}
