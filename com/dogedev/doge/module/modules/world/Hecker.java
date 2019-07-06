package com.dogedev.doge.module.modules.world;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Hecker extends Module {
    private int xPos;
    private int yPos;
    private int zPos;
    private TimeHelper timer;

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Beds");
        options.add("Dragon Eggs");
        Doge.instance.settingsManager.rSetting(new Setting("Hecker Mode", this, "Beds", options));
        Doge.instance.settingsManager.rSetting(new Setting("Radius", this, 6.0f, 0.1f, 10.0f, false));
    }

    public Hecker() {
        super("Hecker", Keyboard.KEY_NONE, Category.WORLD);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (!isToggled()) return;

        String mode = Doge.instance.settingsManager.getSettingByName("Hecker Mode").getValString();
        float radius = (float) Doge.instance.settingsManager.getSettingByName("Radius").getValDouble();

        for (float x = -radius; x < radius; x++) {
            for (float y = radius; y > -radius; y--) {
                for (float z = -radius; z < radius; z++) {
                    xPos = (int) (mc.thePlayer.posX + x);
                    yPos = (int) (mc.thePlayer.posY + y);
                    zPos = (int) (mc.thePlayer.posZ + z);

                    BlockPos bP = new BlockPos(xPos, yPos, zPos);
                    Block b = mc.theWorld.getBlockState(bP).getBlock();

                    if (mode.equalsIgnoreCase("Beds")) {
                        if (b instanceof BlockBed) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bP, mc.thePlayer.getHorizontalFacing()));
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bP, mc.thePlayer.getHorizontalFacing()));
                        }
                    }
                    if (mode.equalsIgnoreCase("Dragon Eggs")) {
                        if (b instanceof BlockDragonEgg) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, bP, mc.thePlayer.getHorizontalFacing()));
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, bP, mc.thePlayer.getHorizontalFacing()));
                        }
                    }
                }
            }
        }
    }

}
