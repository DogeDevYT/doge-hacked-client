package com.dogedev.doge.module.modules.misc;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventTick;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.ChatUtil;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class MurdererFinder extends Module {
    private TimeHelper timer;

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Server");
        options.add("Client");
        Doge.instance.settingsManager.rSetting(new Setting("MurderFinder Mode", this, "Server", options));
    }

    public MurdererFinder() {
        super("MurdererFinder", Keyboard.KEY_NONE, Category.MISC);
    }

    @EventTarget
    public void onTick(EventTick event) {
        String mode = Doge.instance.settingsManager.getSettingByName("MurderFinder Mode").getValString();
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof EntityPlayer && timer.hasTimeReached(15000)) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent != mc.thePlayer && ent.getCurrentEquippedItem() != null && ent.getCurrentEquippedItem().getItem() instanceof ItemSword && !ent.isMurderer) {
                    ent.isMurderer = true;
                    if (mode.equalsIgnoreCase("Server")) {
                        ChatUtil.sendServerChatMessage("Help " + ent.getName() + " just tried to kill me!!1!!");
                    }
                    if (mode.equalsIgnoreCase("Client")) {
                        ChatUtil.send("\247d" + ent.getName() + " \2477is the murderer!");
                    }
                }
            }
        }
    }
}
