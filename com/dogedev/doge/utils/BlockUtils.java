package com.dogedev.doge.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.*;

public class BlockUtils {
    public static boolean isOnLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0, -0.01, 0.0)
                .contract(0.001, 0.001, 0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final BlockPos pos = new BlockPos(var11, var12, var13);
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                    if (!(var14 instanceof BlockAir) && !(var14 instanceof BlockLiquid)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isInLiquid() {
        final AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.contract(0.001, 0.001,
                0.001);
        final int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        final int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        final int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        final int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        final int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        final int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        final Vec3 var10 = new Vec3(0.0, 0.0, 0.0);
        for (int var11 = var4; var11 < var5; ++var11) {
            for (int var12 = var6; var12 < var7; ++var12) {
                for (int var13 = var8; var13 < var9; ++var13) {
                    final BlockPos pos = new BlockPos(var11, var12, var13);
                    final Block var14 = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
                    if (var14 instanceof BlockLiquid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static float[] getDirectionToBlock(int var0, int var1, int var2, EnumFacing var3) {
        EntityEgg var4 = new EntityEgg(Minecraft.getMinecraft().theWorld);
        var4.posX = (double) var0 + 0.5D;
        var4.posY = (double) var1 + 0.5D;
        var4.posZ = (double) var2 + 0.5D;
        var4.posX += (double) var3.getDirectionVec().getX() * 0.25D;
        var4.posY += (double) var3.getDirectionVec().getY() * 0.25D;
        var4.posZ += (double) var3.getDirectionVec().getZ() * 0.25D;
        return getDirectionToEntity(var4);
    }

    private static float[] getDirectionToEntity(Entity var0) {
        return new float[]{getYaw(var0) + Minecraft.getMinecraft().thePlayer.rotationYaw, getPitch(var0) + Minecraft.getMinecraft().thePlayer.rotationPitch};
    }

    public static float getYaw(Entity var0) {
        double var1 = var0.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var3 = var0.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var5;

        if (var3 < 0.0D && var1 < 0.0D) {
            var5 = 90.0D + Math.toDegrees(Math.atan(var3 / var1));
        } else if (var3 < 0.0D && var1 > 0.0D) {
            var5 = -90.0D + Math.toDegrees(Math.atan(var3 / var1));
        } else {
            var5 = Math.toDegrees(-Math.atan(var1 / var3));
        }

        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) var5));
    }

    public static float getPitch(Entity var0) {
        double var1 = var0.posX - Minecraft.getMinecraft().thePlayer.posX;
        double var3 = var0.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var5 = var0.posY - 1.6D + (double) var0.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double var7 = (double) MathHelper.sqrt_double(var1 * var1 + var3 * var3);
        double var9 = -Math.toDegrees(Math.atan(var5 / var7));
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float) var9);
    }
}
