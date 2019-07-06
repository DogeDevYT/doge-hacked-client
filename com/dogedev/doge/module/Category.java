package com.dogedev.doge.module;

public enum Category {
    COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), PLAYER("Player"), MISC("Misc"), WORLD("World");

    private String name;

    Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
