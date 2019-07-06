package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.Event3D;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.RenderUtils;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.tileentity.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ESP2 extends Module {
    private BlockChest blockChest;
    public ESP2() {
        super("StorageESP", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onRender(Event3D event) {
        if (isToggled()) {
            for (Object o : mc.theWorld.loadedTileEntityList) {
                if (o instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) o;
                    this.drawChestESP(chest, chest.getPos().getX(), chest.getPos().getY(), chest.getPos().getZ());
                }
                if (o instanceof TileEntityEnderChest) {
                    TileEntityEnderChest enderChest = (TileEntityEnderChest) o;
                    this.drawEnderChestESP(enderChest, enderChest.getPos().getX(), enderChest.getPos().getY(), enderChest.getPos().getZ());
                }
                if (o instanceof TileEntityFurnace) {
                    TileEntityFurnace furnace = (TileEntityFurnace) o;
                    RenderUtils.blockESP(furnace.getPos(), Color.gray, 1.0, 1.0);
                }
                if (o instanceof TileEntityDispenser) {
                    TileEntityDispenser dispenser = (TileEntityDispenser) o;
                    RenderUtils.blockESP(dispenser.getPos(), Color.gray, 1.0, 1.0);
                }
                if (o instanceof TileEntityDropper) {
                    TileEntityDropper dropper = (TileEntityDropper) o;
                    RenderUtils.blockESP(dropper.getPos(), Color.gray, 1.0, 1.0);
                }
                if (o instanceof TileEntityHopper) {
                    TileEntityHopper hopper = (TileEntityHopper) o;
                    RenderUtils.blockESP(hopper.getPos(), Color.gray, 1.0, 1.0);
                }
            }
            for (Entity e: mc.theWorld.loadedEntityList) {
                if (e instanceof EntityMinecartChest) {
                    RenderUtils.blockESP(e.getPosition(), Color.green, 1.0, 1.0);
                }
                if (e instanceof EntityMinecartFurnace || e instanceof EntityMinecartHopper) {
                    RenderUtils.blockESP(e.getPosition(), Color.gray, 1.0, 1.0);
                }
            }
        }
    }

    public void drawChestESP(TileEntityChest chest, double x, double y, double z) {
        if(this.isToggled()) {
            boolean isAdjacent = chest.adjacentChestChecked;
            if(chest.adjacentChestXPos != null)
            {
                if(blockChest.chestType == 1) { //if is trapped chest
                    RenderUtils.blockESP(chest.getPos(), Color.red, 1.0, 2.0);
                }else{
                    RenderUtils.blockESP(chest.getPos(), Color.green, 1.0, 2.0);
                }
            }

            if(chest.adjacentChestZPos != null)
            {
                if(blockChest.chestType == 1) { //if is trapped chest
                    RenderUtils.blockESP(chest.getPos(), Color.red, 2.0, 1.0);
                }else{
                    RenderUtils.blockESP(chest.getPos(), Color.green, 2.0, 1.0);
                }
            }
            if(chest.adjacentChestZNeg == null && chest.adjacentChestXNeg == null && chest.adjacentChestXPos == null && chest.adjacentChestZPos == null)
            {
                if(blockChest.chestType == 1) { //if is trapped chest
                    RenderUtils.blockESP(chest.getPos(), Color.red, 1.0, 1.0);
                }else{
                    RenderUtils.blockESP(chest.getPos(),
                            Color.green,
                            1.0,
                            1.0);
                }
            }
        }
    }

    public void drawEnderChestESP(TileEntityEnderChest enderChest, double x, double y, double z) {
        if(this.isToggled()) {
            RenderUtils.blockESP(enderChest.getPos(), Color.magenta, 1.0, 1.0);
        }
    }
}
