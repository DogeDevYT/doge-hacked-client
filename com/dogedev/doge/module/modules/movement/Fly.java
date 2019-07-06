package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.sql.Time;
import java.util.ArrayList;

public class Fly extends Module {

    private TimeHelper timer = new TimeHelper();

    public Fly() {
        super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Vanilla");
        options.add("CubeCraft");
        options.add("Old AAC");
        options.add("Rewinside");
        options.add("AAC");
        options.add("CubeCraft2");
        Doge.instance.settingsManager.rSetting(new Setting("Fly Mode", this, "Vanilla", options));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!isToggled()) return;

        String mode = Doge.instance.settingsManager.getSettingByName("Fly Mode").getValString();

        this.setDisplayName("Fly [" + mode + "]");

        if(mode.equalsIgnoreCase("Hypixel")) {
            double y;
            double y1;
            mc.thePlayer.motionY = 0;
            if(mc.thePlayer.ticksExisted % 3 == 0) {
                y = mc.thePlayer.posY - 1.0E-10D;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
            }
            y1 = mc.thePlayer.posY + 1.0E-10D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, y1, mc.thePlayer.posZ);
        }

        if(mode.equalsIgnoreCase("Vanilla"))
            mc.thePlayer.capabilities.isFlying = true;

        if (mode.equalsIgnoreCase("CubeCraft")) {
            if (mc.thePlayer != null && mc.theWorld != null) {
                double teleportV = 1;

                double x = getPosForSetPosX(teleportV);
                double z = getPosForSetPosZ(teleportV);

                mc.timer.timerSpeed = 0.5f;
                mc.thePlayer.motionY = -0.25;

                if (mc.thePlayer.fallDistance >= 0.8f) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + (mc.thePlayer.fallDistance - 0.15), mc.thePlayer.posZ + z);
                    mc.thePlayer.fallDistance = 0;
                }
            }
        }
        if (mode.equalsIgnoreCase("Old AAC")) {
            if (mc.theWorld != null && mc.thePlayer != null) {
                if (mc.thePlayer.fallDistance > 0.0f) mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 2 == 0 ? 0.1 : 0.0;
            }
        }
        if (mode.equalsIgnoreCase("Rewinside")) {
            //By GA - Let's Codes

            if (mc.thePlayer != null && mc.theWorld != null) {
                mc.gameSettings.keyBindLeft.pressed = false;
                mc.gameSettings.keyBindRight.pressed = false;
                mc.gameSettings.keyBindBack.pressed = false;
                mc.gameSettings.keyBindJump.pressed = false;
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
                mc.thePlayer.motionY = 0.0D;
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-10D, mc.thePlayer.posZ);
                mc.thePlayer.onGround = true;
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-10D, mc.thePlayer.posZ, true));
                }
            }
        }
        if (mode.equalsIgnoreCase("AAC")) {
            double teleportV = 8;

            double posX = getPosForSetPosX(teleportV);
            double posZ = getPosForSetPosZ(teleportV);

            mc.timer.timerSpeed = 0.5f;
            if (mc.thePlayer != null && mc.theWorld != null) {
                mc.thePlayer.motionY = 0;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX = posX, mc.thePlayer.posY, mc.thePlayer.posZ = posZ, mc.thePlayer.onGround));
            }
        }
        if (mode.equalsIgnoreCase("CubeCraft2")) {
            double teleportV = 2.5;

            double posX = getPosForSetPosX(teleportV);
            double posZ = getPosForSetPosZ(teleportV);

            mc.timer.timerSpeed = 0.3F;
            mc.thePlayer.motionY = -0.25;

            if (timer.hasTimeReached(150)) {
                mc.thePlayer.setPosition(mc.thePlayer.posX + posX, mc.thePlayer.posY - 0.25, mc.thePlayer.posZ + posZ);
                timer.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        String mode = Doge.instance.settingsManager.getSettingByName("Fly Mode").getValString();

        mc.timer.timerSpeed = 1.0f;

        if(mode.equalsIgnoreCase("Vanilla"))
            mc.thePlayer.capabilities.isFlying = false;
    }

    public double getPosForSetPosX(double value) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * value;
        return x;
    }

    public double getPosForSetPosZ(double value) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double z = Math.cos(yaw) * value;
        return z;
    }
}
