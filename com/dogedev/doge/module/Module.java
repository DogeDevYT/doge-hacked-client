package com.dogedev.doge.module;

import com.dogedev.doge.Doge;
import net.minecraft.client.Minecraft;

public abstract class Module {
    protected Minecraft mc = Minecraft.getMinecraft();

    private String name, displayName;
    private int key;
    private Category category;
    private boolean toggled;

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
        toggled = false;
        this.setup();
    }

    public void onEnable() {
        Doge.instance.eventManager.register(this);
    }
    public void onDisable() {
        Doge.instance.eventManager.unregister(this);
    }
    public void onToggle() {}
    public void toggle() {
        toggled = !toggled;
        onToggle();
        if(toggled) {
            onEnable();
        }
        else {
            onDisable();
        }
    }

    public void setState(boolean toggled) {
        if (toggled) {
            this.toggled = true;
            onEnable();

        } else {
            this.toggled = false;
            onDisable();

        }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public boolean isToggled() {
        return toggled;
    }
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setup() {}
}
