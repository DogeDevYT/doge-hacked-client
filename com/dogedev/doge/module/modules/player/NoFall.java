package com.dogedev.doge.module.modules.player;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.BlockUtils;
import com.dogedev.doge.utils.MovementUtils;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.block.BlockSlime;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class NoFall extends Module {
    private TimeHelper timer = new TimeHelper();
    private double fall;

    public NoFall() {
        super("NoFall", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("LAAC");
        options.add("Basic");
        options.add("Hypixel");
        Doge.instance.settingsManager.rSetting(new Setting("NoFall Mode", this, "Basic", options));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        String mode = Doge.instance.settingsManager.getSettingByName("NoFall Mode").getValString();
        this.fall = 0.0D;
    }

    @EventTarget
    public void onUpdate(EventPostMotionUpdate e) {
        if (isToggled()) {
            String mode = Doge.instance.settingsManager.getSettingByName("NoFall Mode").getValString();
            this.setDisplayName("NoFall" + "[" + mode + "]");
            if(mode.equalsIgnoreCase("Basic")) {
                if (mc.thePlayer.fallDistance > 2F) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
            }
            if (mode.equalsIgnoreCase("LAAC")) {
                if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.fallDistance > 2F) {
                    if (mc.thePlayer.ticksExisted % 6 == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - mc.thePlayer.fallDistance, mc.thePlayer.posZ);
                    }
                }
            }
            if (mode.equalsIgnoreCase("Hypixel")) {
                if (!MovementUtils.isOnGround(0.001D)) {
                    if (mc.thePlayer.motionY < -0.08D)
                        this.fall -= mc.thePlayer.motionY;
                    if (this.fall > 2.0D) {
                        this.fall = 0.0D;

                        mc.thePlayer.onGround = false;
                    }
                }
                this.fall = 0.0D;
            }
        }
    }
}
