package com.dogedev.doge.module.modules.movement;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.Event3D;
import com.dogedev.doge.event.events.EventUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Teleport extends Module {
    private boolean canTP;
    private int delay;
    private BlockPos endPos;

    public MovingObjectPosition getBlinkBlock() {
        Vec3 var4 = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
        Vec3 var5 = mc.thePlayer.getLook(mc.timer.renderPartialTicks);
        Vec3 var6 = var4.addVector(var5.xCoord * 40, var5.yCoord * 40, var5.zCoord * 40);
        return mc.thePlayer.worldObj.rayTraceBlocks(var4, var6, false, false, true);
    }


    public Teleport() {
        super("Teleport", Keyboard.KEY_NONE, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (isToggled()) {
            try {
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                    return;
                }
            } catch (Exception e) {
            }
            if (canTP && Mouse.isButtonDown(1) && !mc.thePlayer.isSneaking() && delay == 0 && mc.inGameHasFocus && getBlinkBlock().entityHit == null && !(getBlock(getBlinkBlock().getBlockPos()) instanceof BlockChest)) {
                event.setCancelled(true);
                endPos = getBlinkBlock().getBlockPos();
                final double[] startPos = {mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
                blinkToPos(startPos, endPos, 0.0, new double[]{0.3, 0.2});
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(endPos.getX() + 0.5, endPos.getY() - 1.0, endPos.getZ() + 0.5, false));
                mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY() + 1, endPos.getZ() + 0.5);
                delay = 5;
                event.setCancelled(false);
            }
            if (delay > 0) {
                --delay;
            }

        }
    }

    @EventTarget
    public void onRender(Event3D event) {
        try {
            final int x = getBlinkBlock().getBlockPos().getX();
            final int y = getBlinkBlock().getBlockPos().getY();
            final int z = getBlinkBlock().getBlockPos().getZ();
            final Block block1 = getBlock(x, y, z);
            final Block block2 = getBlock(x, y + 1, z);
            final Block block3 = getBlock(x, y + 2, z);
            final boolean blockBelow = !(block1 instanceof BlockSign) && block1.getMaterial().isSolid();
            final boolean blockLevel = !(block2 instanceof BlockSign) && block1.getMaterial().isSolid();
            final boolean blockAbove = !(block3 instanceof BlockSign) && block1.getMaterial().isSolid();
            if (getBlock(getBlinkBlock().getBlockPos()).getMaterial() != Material.air && blockBelow && blockLevel && blockAbove && !(getBlock(getBlinkBlock().getBlockPos()) instanceof BlockChest)) {
                canTP = true;
            } else {
                canTP = false;
            }
        } catch (Exception e) {

        }

    }

    public static Block getBlock(final int x, final int y, final int z) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static Block getBlock(final BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack, final double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        try {
            final double endX = endPos.getX() + 0.5;
            final double endY = endPos.getY() + 1.0;
            final double endZ = endPos.getZ() + 0.5;

            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count > 120) {
                    break;
                }
                final boolean next = false;
                final double diffX = curX - endX;
                final double diffY = curY - endY;
                final double diffZ = curZ - endZ;
                final double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
                if (diffX < 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX += offset;
                    } else {
                        curX += Math.abs(diffX);
                    }
                }
                if (diffX > 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX -= offset;
                    } else {
                        curX -= Math.abs(diffX);
                    }
                }
                if (diffY < 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY += 0.25;
                    } else {
                        curY += Math.abs(diffY);
                    }
                }
                if (diffY > 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY -= 0.25;
                    } else {
                        curY -= Math.abs(diffY);
                    }
                }
                if (diffZ < 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ += offset;
                    } else {
                        curZ += Math.abs(diffZ);
                    }
                }
                if (diffZ > 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ -= offset;
                    } else {
                        curZ -= Math.abs(diffZ);
                    }
                }
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
                ++count;

            }
        } catch (Exception e) {

        }
    }

}
