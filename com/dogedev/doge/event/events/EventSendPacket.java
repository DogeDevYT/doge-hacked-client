package com.dogedev.doge.event.events;

import com.dogedev.doge.event.Event;
import net.minecraft.network.Packet;
import sun.security.pkcs11.Secmod;

public class EventSendPacket extends Event {
    private Packet packet;

    public EventSendPacket(Packet packet) {
        packet = null;
        setPacket(packet);
    }

    public Packet getPacket() {
        return packet;
    }
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
