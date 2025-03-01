package com.dogedev.doge.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class MovementUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    public static double defaultSpeed() {
        double baseSpeed = 0.2873D;
        if ((Minecraft.getMinecraft()).thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = (Minecraft.getMinecraft()).thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();



            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }
    public static void strafe(double speed) {
        float a = mc.thePlayer.rotationYaw * 0.017453292F;
        float l = mc.thePlayer.rotationYaw * 0.017453292F - 4.712389F;
        float r = mc.thePlayer.rotationYaw * 0.017453292F + 4.712389F;
        float rf = mc.thePlayer.rotationYaw * 0.017453292F + 0.5969026F;
        float lf = mc.thePlayer.rotationYaw * 0.017453292F + -0.5969026F;
        float lb = mc.thePlayer.rotationYaw * 0.017453292F - 2.3876104F;
        float rb = mc.thePlayer.rotationYaw * 0.017453292F - -2.3876104F;
        if (mc.gameSettings.keyBindForward.pressed) {
            if (mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindRight.pressed) {
                mc.thePlayer.motionX -= MathHelper.sin(lf) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(lf) * speed;
            } else if (mc.gameSettings.keyBindRight.pressed && !mc.gameSettings.keyBindLeft.pressed) {
                mc.thePlayer.motionX -= MathHelper.sin(rf) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(rf) * speed;
            } else {
                mc.thePlayer.motionX -= MathHelper.sin(a) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(a) * speed;
            }
        } else if (mc.gameSettings.keyBindBack.pressed) {
            if (mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindRight.pressed) {
                mc.thePlayer.motionX -= MathHelper.sin(lb) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(lb) * speed;
            } else if (mc.gameSettings.keyBindRight.pressed && !mc.gameSettings.keyBindLeft.pressed) {
                mc.thePlayer.motionX -= MathHelper.sin(rb) * speed;
                mc.thePlayer.motionZ += MathHelper.cos(rb) * speed;
            } else {
                mc.thePlayer.motionX += MathHelper.sin(a) * speed;
                mc.thePlayer.motionZ -= MathHelper.cos(a) * speed;
            }
        } else if (mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindRight.pressed && !mc.gameSettings.keyBindForward.pressed && !mc.gameSettings.keyBindBack.pressed) {
            mc.thePlayer.motionX += MathHelper.sin(l) * speed;
            mc.thePlayer.motionZ -= MathHelper.cos(l) * speed;
        } else if (mc.gameSettings.keyBindRight.pressed && !mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindForward.pressed && !mc.gameSettings.keyBindBack.pressed) {
            mc.thePlayer.motionX += MathHelper.sin(r) * speed;
            mc.thePlayer.motionZ -= MathHelper.cos(r) * speed;
        }
    }

    public static void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += ((forward > 0.0D) ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += ((forward > 0.0D) ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1.0D;
                } else if (forward < 0.0D) {
                    forward = -1.0D;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
        }
    }

    public static boolean checkTeleport(double x, double y, double z, double distBetweenPackets) {
        double distx = mc.thePlayer.posX - x;
        double disty = mc.thePlayer.posY - y;
        double distz = mc.thePlayer.posZ - z;
        double dist = Math.sqrt(mc.thePlayer.getDistanceSq(x, y, z));
        double distanceEntreLesPackets = distBetweenPackets;
        double nbPackets = (Math.round(dist / distanceEntreLesPackets + 0.49999999999D) - 1L);

        double xtp = mc.thePlayer.posX;
        double ytp = mc.thePlayer.posY;
        double ztp = mc.thePlayer.posZ;
        for (int i = 1; i < nbPackets; i++) {
            double xdi = (x - mc.thePlayer.posX) / nbPackets;
            xtp += xdi;

            double zdi = (z - mc.thePlayer.posZ) / nbPackets;
            ztp += zdi;

            double ydi = (y - mc.thePlayer.posY) / nbPackets;
            ytp += ydi;
            AxisAlignedBB bb = new AxisAlignedBB(xtp - 0.3D, ytp, ztp - 0.3D, xtp + 0.3D, ytp + 1.8D, ztp + 0.3D);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return false;
            }
        }

        return true;
    }


    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        }
        return false;
    }

    public static int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }


    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) { return (Minecraft.getMinecraft()).theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock(); }


    public static Block getBlockAtPosC(double x, double y, double z) {
        EntityPlayerSP entityPlayerSP = (Minecraft.getMinecraft()).thePlayer;
        return (Minecraft.getMinecraft()).theWorld.getBlockState(new BlockPos(entityPlayerSP.posX + x, entityPlayerSP.posY + y, entityPlayerSP.posZ + z)).getBlock();
    }


    public static float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = block.getX() + 0.5D - mc.thePlayer.posX + face.getFrontOffsetX() / 2.0D;
        double z = block.getZ() + 0.5D - mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0D;
        double y = block.getY() + 0.5D;
        double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0D / Math.PI);
        if (yaw < 0.0F) {
            yaw += 360.0F;
        }
        return new float[] { yaw, pitch };
    }
    public static boolean isBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX - 0.3D, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ + 0.3D, mc.thePlayer.posX + 0.3D, mc.thePlayer.posY + 2.5D, mc.thePlayer.posZ - 0.3D);

        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty();
    }
    public static boolean isCollidedH(double dist) {
        AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX - 0.3D, mc.thePlayer.posY + 2.0D, mc.thePlayer.posZ + 0.3D, mc.thePlayer.posX + 0.3D, mc.thePlayer.posY + 3.0D, mc.thePlayer.posZ - 0.3D);

        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3D + dist, 0.0D, 0.0D)).isEmpty())
            return true;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3D - dist, 0.0D, 0.0D)).isEmpty())
            return true;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0D, 0.0D, 0.3D + dist)).isEmpty())
            return true;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0D, 0.0D, -0.3D - dist)).isEmpty()) {
            return true;
        }
        return false;
    }
    public static boolean isRealCollidedH(double dist) {
        AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX - 0.3D, mc.thePlayer.posY + 0.5D, mc.thePlayer.posZ + 0.3D, mc.thePlayer.posX + 0.3D, mc.thePlayer.posY + 1.9D, mc.thePlayer.posZ - 0.3D);

        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.3D + dist, 0.0D, 0.0D)).isEmpty())
            return true;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(-0.3D - dist, 0.0D, 0.0D)).isEmpty())
            return true;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0D, 0.0D, 0.3D + dist)).isEmpty())
            return true;
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb.offset(0.0D, 0.0D, -0.3D - dist)).isEmpty()) {
            return true;
        }
        return false;
    }
}
