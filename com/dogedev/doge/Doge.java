package com.dogedev.doge;

import com.dogedev.doge.event.EventManager;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventKey;
import com.dogedev.doge.module.ModuleManager;
import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import org.lwjgl.opengl.Display;

public class Doge {
    public String name = "Doge", version = "1.5-REL", creator = "Salty_Scrimp";

    public static Doge instance = new Doge();

    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ClickGUI clickGui;

    public void startClient() {
        settingsManager = new SettingsManager();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        clickGui = new ClickGUI();

        System.out.println("[" + name + "] Starting client, b" + version + ", created by " + creator);
        Display.setTitle(name + " v" + version);

        System.out.println("woof");

        eventManager.register(this);
    }

    public void stopClient() {
        eventManager.unregister(this);
    }

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }
}
