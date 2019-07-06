package com.dogedev.doge.module.modules.player;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventSendPacket;
import com.dogedev.doge.event.events.EventTick;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiLagback extends Module {
    private List<Packet> packets = new CopyOnWriteArrayList<>();
    private boolean sending;
    private int delay;


    public AntiLagback() {
        super("AntiLagback", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (!isToggled()) return;
        delay += 1;
        if (delay >= 1)
        {
            sending = true;
            sendPackets();
            delay = 0;
        }
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        if (!isToggled()) return;
        if (sending) {
            return;
        }
        if (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C08PacketPlayerBlockPlacement || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C02PacketUseEntity) {
            event.setCancelled(true);
        }
        boolean input = (mc.gameSettings.keyBindForward.isPressed()) || (mc.gameSettings.keyBindBack.isPressed()) || (mc.gameSettings.keyBindRight.isPressed()) || (mc.gameSettings.keyBindLeft.isPressed());
        if (input && (event.getPacket() instanceof C03PacketPlayer)) {
            packets.add(event.getPacket());
        }
        if (event.getPacket() instanceof C02PacketUseEntity) {
            packets.add(event.getPacket());
            mc.thePlayer.rotationYaw -= 180.0F;
        }
    }

    public void sendPackets()
    {
        if (packets.size() > 0) {
            for (Packet packet : packets)
            {
                if (((packet instanceof C02PacketUseEntity)) || ((packet instanceof C08PacketPlayerBlockPlacement)) || ((packet instanceof C07PacketPlayerDigging))) {
                    mc.thePlayer.swingItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(packet);
            }
        }
        packets.clear();
        sending = false;
    }

    public void onDisable()
    {
        super.onDisable();
        sending = true;
        sendPackets();
    }

}
