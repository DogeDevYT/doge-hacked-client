package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.MovementUtils;
import com.dogedev.doge.utils.PlayerUtils;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class LongJump extends Module {
    private static Minecraft mc = Minecraft.getMinecraft();
    private float air;
    private float groundTicks;
    private int stage;

    @Override
    public void onEnable() {
        super.onEnable();
        air = 0.0f;
        stage = 0;
        groundTicks = 0.0f;
    }

    public static void vclip1() {
        double X = mc.thePlayer.posX;
        double Y = mc.thePlayer.posY;
        double Z = mc.thePlayer.posZ;
        double y = 0.42D;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + y, Z, false));
        y += 0.333D;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + y, Z, false));
        y += 0.247D;
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(X, Y + y, Z, false));
        y += 0.164D;
        mc.thePlayer.setPosition(X, Y + y, Z);
        mc.thePlayer.onGround = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        String mode = Doge.instance.settingsManager.getSettingByName("LongJump Mode").getValString();
        switch (mode) {
            default:
                mc.thePlayer.motionX *= 0.0D;
                mc.thePlayer.motionZ *= 0.0D;
                mc.thePlayer.jumpMovementFactor = 0.0F;
                break;
        }
        mc.timer.timerSpeed = 1.0F;
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Basic");
        options.add("Hypixel");
        Doge.instance.settingsManager.rSetting(new Setting("LongJump Mode", this, "Basic", options));
        Doge.instance.settingsManager.rSetting(new Setting("NCPGlide", this, false));
        Doge.instance.settingsManager.rSetting(new Setting("OneJump", this, false));
    }

    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        boolean oneJump = Doge.instance.settingsManager.getSettingByName("OneJump").getValBoolean();
        boolean glide = Doge.instance.settingsManager.getSettingByName("NCPGlide").getValBoolean();
        String mode = Doge.instance.settingsManager.getSettingByName("LongJump Mode").getValString();
        setDisplayName("LongJump [" + mode + "]");
        if (mode.equalsIgnoreCase("Basic")) {
            if((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) && mc.gameSettings.keyBindJump.isKeyDown()) {
                float dir = mc.thePlayer.rotationYaw + ((mc.thePlayer.moveForward < 0) ? 180 : 0) + ((mc.thePlayer.moveStrafing > 0) ? (-90F * ((mc.thePlayer.moveForward < 0) ? -.5F : ((mc.thePlayer.moveForward > 0) ? .4F : 1F))) : 0);
                float xDir = (float)Math.cos((dir + 90F) * Math.PI / 180);
                float zDir = (float)Math.sin((dir + 90F) * Math.PI / 180);
                if(mc.thePlayer.isCollidedVertically && (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) && mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionX = xDir * .29F;
                    mc.thePlayer.motionZ = zDir * .29F;
                }
                if(mc.thePlayer.motionY == .33319999363422365 && (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown())) {
                    if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                        mc.thePlayer.motionX = xDir * 1.34;
                        mc.thePlayer.motionZ = zDir * 1.34;
                    } else {
                        mc.thePlayer.motionX = xDir * 1.261;
                        mc.thePlayer.motionZ = zDir * 1.261;
                    }
                }
            }
        }
        if (mode.equalsIgnoreCase("Hypixel")) {
            float x2 = 0.7F + MovementUtils.getSpeedEffect() * 0.45F;
            if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                if (this.groundTicks > 0.0F && oneJump) {
                    this.groundTicks = 0.0F;
                    toggle();
                    return;
                }
                this.groundTicks++;
                MovementUtils.setMotion(0.15D);
                mc.thePlayer.jump();
                this.stage = 1;
            }
            if (MovementUtils.isOnGround(0.001D)) {
                this.air = 0.0F;
            } else {
                if (mc.thePlayer.isCollidedVertically)
                    this.stage = 0;
                if (this.stage > 0 && this.stage <= 3 && glide) {
                    mc.thePlayer.onGround = true;
                }
                double speed = 0.95D + MovementUtils.getSpeedEffect() * 0.2D - (this.air / 25.0F);
                if (glide) {
                    speed = 1.1D + (MovementUtils.getSpeedEffect() * 0.2F) - (this.air / 20.0F);
                }
                if (speed < MovementUtils.defaultSpeed() - 0.05D) {
                    speed = MovementUtils.defaultSpeed() - 0.05D;
                }
                if (this.stage < 4 && glide)
                    speed = MovementUtils.defaultSpeed();
                MovementUtils.setMotion(speed);
                if (glide) {
                    mc.thePlayer.motionY = getMotion(this.stage);
                } else {
                    mc.thePlayer.motionY = getOldMotion(this.stage);
                }
                if (this.stage > 0) {
                    this.stage++;
                }
                this.air += x2;
            }
        }
    }

    double getMotion(int stage) {
        boolean isMoving = (mc.thePlayer.moveStrafing != 0.0F || mc.thePlayer.moveForward != 0.0F);
        double[] mot = { 0.396D, -0.122D, -0.1D, 0.423D, 0.35D, 0.28D, 0.217D, 0.15D, 0.025D, -0.00625D, -0.038D, -0.0693D, -0.102D, -0.13D, -0.018D, -0.1D, -0.117D, -0.14532D, -0.1334D, -0.1581D, -0.183141D, -0.170695D, -0.195653D, -0.221D, -0.209D, -0.233D, -0.25767D, -0.314917D, -0.371019D, -0.426D };


        stage--;
        if (stage >= 0 && stage < mot.length) {
            return mot[stage];
        }

        return mc.thePlayer.motionY;
    }

    double getOldMotion(int stage) {
        boolean isMoving = (mc.thePlayer.moveStrafing != 0.0F || mc.thePlayer.moveForward != 0.0F);
        double[] mot = { 0.345D, 0.2699D, 0.183D, 0.103D, 0.024D, -0.008D, -0.04D, -0.072D, -0.104D, -0.13D, -0.019D, -0.097D };
        double[] notMoving = { 0.345D, 0.2699D, 0.183D, 0.103D, 0.024D, -0.008D, -0.04D, -0.072D, -0.14D, -0.17D, -0.019D, -0.13D };
        stage--;
        if (stage >= 0 && stage < mot.length) {
            if ((mc.thePlayer.moveStrafing == 0.0F && mc.thePlayer.moveForward == 0.0F) || mc.thePlayer.isCollidedHorizontally) {
                return notMoving[stage];
            }
            return mot[stage];
        }
        return mc.thePlayer.motionY;
    }
}
