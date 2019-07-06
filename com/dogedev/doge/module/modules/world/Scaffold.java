package com.dogedev.doge.module.modules.world;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.Event3D;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.event.events.EventRenderGui;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.BlockUtils;
import com.dogedev.doge.utils.RayCastUtil;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.lang.reflect.Field;

public class Scaffold extends Module {
    private BlockPos currentPos;
    private EnumFacing currentFacing;
    private boolean rotated = false;
    private TimeHelper timer = new TimeHelper();
    private float[] rotations = new float[2];

    @Override
    public void setup() {
        Doge.instance.settingsManager.rSetting(new Setting("Delay", this, 0, 0, 2000, true));
        Doge.instance.settingsManager.rSetting(new Setting("Eagle", this, false));
        Doge.instance.settingsManager.rSetting(new Setting("RayCast", this, true));
        Doge.instance.settingsManager.rSetting(new Setting("KeepRotations", this, true));
    }

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.WORLD);
    }

    @EventTarget
    public void onRender(EventRenderGui event) {
        ScaledResolution res = new ScaledResolution(mc);
        if (isToggled()) {
            mc.fontRendererObj.drawStringWithShadow("" + getBlockCount(), res.getScaledWidth() * 0.45f, res.getScaledHeight() * 0.5f, rainbow(0));
        }
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!isToggled()) return;

        boolean eagle = Doge.instance.settingsManager.getSettingByName("Eagle").getValBoolean();
        boolean rayCast = Doge.instance.settingsManager.getSettingByName("RayCast").getValBoolean();
        boolean keepRotations = Doge.instance.settingsManager.getSettingByName("KeepRotations").getValBoolean();

        if (eagle) {
            if (rotated) {
                mc.gameSettings.keyBindSneak.pressed = true;
            } else {
                mc.gameSettings.keyBindSneak.pressed = false;
            }
        }

        rotated = false;
        currentPos = null;
        currentFacing = null;

        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
            setBlockAndFacing(pos);

            if (currentPos != null) {
                float facing[] = BlockUtils.getDirectionToBlock(currentPos.getX(), currentPos.getY(), currentPos.getZ(), currentFacing);

                float yaw = facing[0];
                float pitch = Math.min(90, facing[1] + 9);

                rotations[0] = yaw;
                rotations[1] = pitch;

                rotated = !rayCast || rayTrace(yaw, pitch);

                event.setYaw(yaw);
                event.setPitch(pitch);
            }
        } else {
            if (keepRotations) {
                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
            }
        }
        mc.thePlayer.rotationYawHead = event.getYaw();
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        if (!isToggled()) return;

        int delay = (int) Doge.instance.settingsManager.getSettingByName("Delay").getValDouble();
        boolean eagle = Doge.instance.settingsManager.getSettingByName("Eagle").getValBoolean();

        if (eagle) {
            if (rotated) {
                mc.gameSettings.keyBindSneak.pressed = true;
            } else {
                mc.gameSettings.keyBindSneak.pressed = false;
            }
        }

        if (currentPos != null) {
            if (timer.hasTimeReached(delay) && rotated) {
                if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), currentPos, currentFacing, new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()))) {
                        timer.setLastMS();
                        mc.thePlayer.swingItem();

                        if (!eagle) {
                            mc.thePlayer.motionX = 0;
                            mc.thePlayer.motionZ = 0;
                        } else {
                            mc.thePlayer.motionX *= 0.7;
                            mc.thePlayer.motionZ *= 0.7;
                        }
                    }
                }
            }
        }
    }

    private boolean rayTrace(float yaw, float pitch) {
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
        Vec3 vec31 = RayCastUtil.getVectorForRotation(pitch, yaw);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * 5, vec31.yCoord * 5, vec31.zCoord * 5);


        MovingObjectPosition result = mc.theWorld.rayTraceBlocks(vec3, vec32, false);


        return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && currentPos.equals(result.getBlockPos());
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
    }


    private void setBlockAndFacing(BlockPos var1) {
        if (mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, -1, 0);
            currentFacing = EnumFacing.UP;
        } else if (mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(-1, 0, 0);
            currentFacing = EnumFacing.EAST;
        } else if (mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(1, 0, 0);
            currentFacing = EnumFacing.WEST;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, -1);
            currentFacing = EnumFacing.SOUTH;
        } else if (mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.currentPos = var1.add(0, 0, 1);
            currentFacing = EnumFacing.NORTH;
        } else {
            currentPos = null;
            currentFacing = null;
        }
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }

    private static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.55f, 0.8f).getRGB();
    }

}
