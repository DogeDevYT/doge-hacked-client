package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.Doge;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import de.Hero.settings.Setting;
import net.minecraft.block.Block;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class XRay extends Module {
    public static ArrayList<Block> xrayBlocks = new ArrayList();

    @Override
    public void setup() {
        Doge.instance.settingsManager.rSetting(new Setting("Opacity", this, 5, 0, 255, true));
    }

    public XRay() {
        super("XRay", Keyboard.KEY_NONE, Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.renderGlobal.loadRenderers();
    }
}
