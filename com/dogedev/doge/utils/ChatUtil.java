package com.dogedev.doge.utils;

import com.dogedev.doge.Doge;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatUtil {
    private static final String PREFIX = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.GOLD + Doge.instance.name + EnumChatFormatting.GRAY + "]";

    public static void send(final String s) {
        JsonObject object = new JsonObject();
        object.addProperty("text", s);
        Minecraft.getMinecraft().thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(object.toString()));
    }

    public static void sendServerChatMessage(String s) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(s);
    }

    public static void success(String s) {
        info(s);
    }

    public static void info(String s) {
        send(PREFIX + s);
    }
}
