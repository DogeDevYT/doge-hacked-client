package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import de.Hero.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Speed extends Module {
    public Speed() {
        super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Y-Port");
        options.add("AAC");
        options.add("Hypixel BHop");
        options.add("AAC ZOOM");
        Doge.instance.settingsManager.rSetting(new Setting("Speed Mode", this, "Y-Port", options));
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!isToggled()) return;

        String mode = Doge.instance.settingsManager.getSettingByName("Speed Mode").getValString();
        this.setDisplayName("Speed" + "[" + mode + "]");
        if(mode.equalsIgnoreCase("Y-Port")) {
            if(isOnLiquid())
                return;
            if(mc.thePlayer.onGround && (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
                if(mc.thePlayer.ticksExisted % 2 != 0)
                    event.y += .4;
                setSpeed(mc.thePlayer.ticksExisted % 2 == 0 ? .45F : .2F);
                mc.timer.timerSpeed = 1.095F;
            }
        }
        if (mode.equalsIgnoreCase("AAC")) {
            boolean boost = Math.abs(mc.thePlayer.rotationYawHead - mc.thePlayer.rotationYaw) < 90;
            if (mc.thePlayer.moveForward > 0 && mc.thePlayer.hurtTime < 5) {
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.405;
                    float f = (float) getDirection();
                    mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.2F);
                    mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.2F);
                } else {
                    double currentSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
                    double speed = boost ? 1.0064 : 1.001;

                    double direction = getDirection();

                    mc.thePlayer.motionX = -Math.sin(direction) * speed * currentSpeed;
                    mc.thePlayer.motionZ = Math.cos(direction) * speed * currentSpeed;
                }
            }
        }
        if (mode.equalsIgnoreCase("Hypixel BHop")) {
            if (mc.theWorld != null && mc.thePlayer != null) {
                mc.gameSettings.keyBindJump.pressed = false;
                if (mc.gameSettings.keyBindForward.pressed && !mc.thePlayer.isCollidedHorizontally) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        mc.timer.timerSpeed = 1.05F;
                        mc.thePlayer.motionX *= 1.07F;
                        mc.thePlayer.motionZ *= 1.07F;
                    } else {
                        mc.thePlayer.jumpMovementFactor = 0.0265F;
                        mc.timer.timerSpeed = 1.4F;
                        double direction = getDirection2();
                        double speed = 1;
                        double currentMotion = Math.sqrt(
                                mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);

                        mc.thePlayer.motionX = -Math.sin(direction) * speed * currentMotion;
                        mc.thePlayer.motionZ = Math.cos(direction) * speed * currentMotion;
                    }
                }
            }
        }
        if (mode.equalsIgnoreCase("AAC ZOOM")) {
            boolean boost = Math.abs(mc.thePlayer.rotationYawHead - mc.thePlayer.rotationYaw) < 90;
            if (mc.thePlayer.moveForward > 0 && mc.thePlayer.hurtTime < 5) {
                mc.timer.timerSpeed = 1.025f;
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.405;
                    float f = (float) getDirection();
                    mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.2F);
                    mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.2F);
                } else {
                    double currentSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
                    double speed = boost ? 1.0064 : 1.001;

                    double direction = getDirection();

                    mc.thePlayer.motionX = -Math.sin(direction) * speed * currentSpeed;
                    mc.thePlayer.motionZ = Math.cos(direction) * speed * currentSpeed;
                }
            }
        }
    }

    private boolean isOnLiquid() {
        boolean onLiquid = false;
        final int y = (int)(mc.thePlayer.boundingBox.minY - .01);
        for(int x = MathHelper.floor_double(mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for(int z = MathHelper.floor_double(mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if(block != null && !(block instanceof BlockAir)) {
                    if(!(block instanceof BlockLiquid))
                        return false;
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }

    public void setSpeed(float speed) {
        mc.thePlayer.motionX = (-(Math.sin(getDirection()) * speed));
        mc.thePlayer.motionZ = (Math.cos(getDirection()) * speed);
    }

    public static double getDirection() {
        Minecraft mc = Minecraft.getMinecraft();

        float yaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0.0F) yaw += 180.0F;

        float forward = 1.0F;

        if (mc.thePlayer.moveForward < 0.0F) forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0.0F) forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0.0F) yaw -= 90.0F * forward;

        if (mc.thePlayer.moveStrafing < 0.0F) yaw += 90.0F * forward;

        return Math.toRadians(yaw);
    }

    public static float getDirection2() {
        Minecraft mc = Minecraft.getMinecraft();
        float var1 = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0.0F) { // If the player walks backward
            var1 += 180.0F;
        }

        float forward = 1.0F;

        if (mc.thePlayer.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (mc.thePlayer.moveForward > 0.0F) {
            forward = 0.5F;
        }

        if (mc.thePlayer.moveStrafing > 0.0F) {
            var1 -= 90.0F * forward;
        }

        if (mc.thePlayer.moveStrafing < 0.0F) {
            var1 += 90.0F * forward;
        }
        var1 *= 0.017453292F; // Faster version of Math.toRadians (x * 1 / 180 * PI)
        return var1;
    }
}