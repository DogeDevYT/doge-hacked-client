package com.dogedev.doge.module.modules.combat;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventSendPacket;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.PlayerUtils;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", Keyboard.KEY_NONE, Category.COMBAT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Packet");
        options.add("MiniJump");
        options.add("Jump");
        Doge.instance.settingsManager.rSetting(new Setting("Criticals Mode", this, "Packet", options));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Doge.instance.settingsManager.getSettingByName("Criticals Mode").getValString();
        this.setDisplayName("Criticals" + "[" + mode + "]");
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        String mode = Doge.instance.settingsManager.getSettingByName("Criticals Mode").getValString();

        if(canCrit()) {
            if (event.getPacket() instanceof C02PacketUseEntity) {
                C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
                if(packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
                    if(mode.equalsIgnoreCase("Packet")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + .1625, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4.0E-6, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-6, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                    }
                    if(mode.equalsIgnoreCase("MiniJump")) {
                        mc.thePlayer.jump();
                        mc.thePlayer.motionY -= .30000001192092879;
                    }
                    if (mode.equalsIgnoreCase("Jump")) {
                        mc.thePlayer.jump();
                    }
                }
            }
        }
    }

    private boolean canCrit() {
        return !PlayerUtils.isInLiquid() && mc.thePlayer.onGround;
    }
}