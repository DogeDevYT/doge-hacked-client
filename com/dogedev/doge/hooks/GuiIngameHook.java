package com.dogedev.doge.hooks;

import com.dogedev.doge.Doge;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiIngameHook extends GuiIngame {

    public GuiIngameHook(Minecraft mcIn) {
        super(mcIn);
    }

    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);
        FontRenderer fontRenderer = mc.fontRendererObj;
        ScaledResolution res = new ScaledResolution(this.mc);
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();

        GL11.glScaled(2.0, 2.0, 2.0);
        int i = fontRenderer.drawString(Doge.instance.name, 2, 2, rainbow(0), true);
        GL11.glScaled(0.5, 0.5, 0.5);

        fontRenderer.drawString(Doge.instance.version, i * 2, fontRenderer.FONT_HEIGHT * 2 - 7, rainbow(100), true);
        fontRenderer.drawString("by " + Doge.instance.creator, 4, fontRenderer.FONT_HEIGHT * 2 + 2, rainbow(200), true);

        AtomicInteger offset = new AtomicInteger(3);
        AtomicInteger index = new AtomicInteger();

        for (Module m : ModuleManager.getModules()) {
            if (m.isToggled()) {
                Gui.drawRect(res.getScaledWidth() - fontRenderer.getStringWidth(m.getDisplayName()) - 7, offset.get() - 2, res.getScaledWidth(), offset.get() + 10, new Color(0, 0, 0, 161).getRGB());
                Gui.drawRect(res.getScaledWidth() - 3, offset.get() - 2, res.getScaledWidth(), offset.get() + 10, rainbow(index.get() * 100));
                fontRenderer.drawString(m.getDisplayName(), res.getScaledWidth() - fontRenderer.getStringWidth(m.getDisplayName()) - 5, offset.get(), rainbow(index.get() * 100), true);

                offset.addAndGet(fontRenderer.FONT_HEIGHT + 2);
                index.getAndIncrement();
            }
        }
    }

    private static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.55f, 0.8f).getRGB();
    }
}
