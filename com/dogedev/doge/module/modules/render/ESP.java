package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.Event3D;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.RenderUtils;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ESP extends Module {
    private BlockChest blockChest;
    public ESP() {
        super("EntityESP", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onRender(Event3D event) {
        if (!isToggled()) return;

        for(Object e: mc.theWorld.loadedEntityList){
            if(e instanceof EntityPlayer && e != mc.thePlayer) {
                RenderUtils.drawEntityESP((Entity) e, Color.BLUE);
            }
            if (e instanceof EntityPlayer && mc.thePlayer.isOnSameTeam((EntityPlayer) e) && e != mc.thePlayer) {
                RenderUtils.drawEntityESP((Entity) e, Color.MAGENTA);
            }
            if(e instanceof EntityMob) {
                RenderUtils.drawEntityESP((Entity) e, Color.RED);
            }
            if (e instanceof EntityAnimal) {
                RenderUtils.drawEntityESP((Entity) e, Color.GREEN);
            }
        }
    }

}
