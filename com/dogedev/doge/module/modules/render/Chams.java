package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventRenderEntity;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Chams extends Module {
    public Chams() {
        super("Chams", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onRenderEntity(EventRenderEntity event) {
        if (event.isPre() && event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0F, -1100000.0F);
        } else if (event.isPost() && event.getEntity() instanceof net.minecraft.entity.player.EntityPlayer) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0F, 1100000.0F);
        }
    }
}
