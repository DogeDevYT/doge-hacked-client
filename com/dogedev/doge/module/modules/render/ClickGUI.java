package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.Doge;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("New");
        options.add("JellyLike");
        Doge.instance.settingsManager.rSetting(new Setting("Design", this, "New", options));
        Doge.instance.settingsManager.rSetting(new Setting("Sound", this, false));
        Doge.instance.settingsManager.rSetting(new Setting("GuiRed", this, 0, 0 ,255, true));
        Doge.instance.settingsManager.rSetting(new Setting("GuiGreen", this, 94, 0 ,255, true));
        Doge.instance.settingsManager.rSetting(new Setting("GuiBlue", this, 255, 0 ,255, true));
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Doge.instance.clickGui);
        toggle();

        super.onEnable();
    }
}
