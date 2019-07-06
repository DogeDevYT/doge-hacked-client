package com.dogedev.doge.module.modules.combat;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.event.events.EventReceivePacket;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class AutoPot extends Module {
    public static long delay;
    private float oldYaw;
    private float oldPitch;
    public static int pots;
    private TimeHelper timer = new TimeHelper();
    private boolean needsToPot;
    private static boolean potting;

    @Override
    public void setup() {
        Doge.instance.settingsManager.rSetting(new Setting("Pot Health", this, 5.0, 1.0, 10.0, false));
    }

    private double getPotHealth() {
        return Doge.instance.settingsManager.getSettingByName("Pot Health").getValDouble();
    }

    public AutoPot() {
        super("AutoPot", Keyboard.KEY_NONE, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        setDisplayName("AutoPot - " + pots);
    }

    @EventTarget
    public void EventPreMotionUpdates(final EventPreMotionUpdate event) {
        if (updateCounter() == 0) {
            return;
        }
        if (timer.hasReached(Long.valueOf(delay)) && needsToPot && doesHotbarHavePots()) {
            oldYaw = mc.thePlayer.rotationYaw;
            oldPitch = mc.thePlayer.rotationPitch;
            potting = true;
            mc.thePlayer.rotationYaw = -mc.thePlayer.rotationYaw;
            mc.thePlayer.rotationPitch = 90.0f;
        }
    }

    @EventTarget
    public void onPost(final EventPostMotionUpdate event) {
        needsToPot = (mc.thePlayer.getHealth() <= getPotHealth());
        if (timer.hasReached(Long.valueOf(delay)) && needsToPot) {
            if (doesHotbarHavePots()) {
                if (potting) {
                    if (needsToPot) {
                        for (int i = 36; i < 45; ++i) {
                            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                            if (stack != null && isStackSplashHealthPot(stack)) {
                                final int oldSlot = mc.thePlayer.inventory.currentItem;
                                mc.thePlayer.inventory.currentItem = i - 36;
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                                mc.thePlayer.inventory.currentItem = oldSlot;
                                mc.thePlayer.rotationYaw = oldYaw;
                                mc.thePlayer.rotationPitch = oldPitch;
                                needsToPot = false;
                                potting = false;
                                break;
                            }
                        }
                    }
                    potting = false;
                    timer.reset();
                }
            }
            else {
                getPotsFromInventory();
            }
        }
        setDisplayName("AutoPot - " + pots);
    }

    @EventTarget
    public void onPacketReceive(final EventReceivePacket e) {
        if (e.getPacket() instanceof S06PacketUpdateHealth) {
            final S06PacketUpdateHealth packetUpdateHealth = (S06PacketUpdateHealth)e.getPacket();
            if (!needsToPot) {
                needsToPot = (packetUpdateHealth.getHealth() <= getPotHealth());
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        potting = false;
        needsToPot = false;
    }

    private boolean doesHotbarHavePots() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && isStackSplashHealthPot(stack)) {
                return true;
            }
        }
        return false;
    }

    private void getPotsFromInventory() {
        for (int i = 9; i < 36; ++i) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && isStackSplashHealthPot(stack)) {
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 1, 2, mc.thePlayer);
                break;
            }
        }
    }

    private boolean isStackSplashHealthPot(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion)stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final Object o : potion.getEffects(stack)) {
                    final PotionEffect effect = (PotionEffect)o;
                    if (effect.getPotionID() == Potion.heal.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int updateCounter() {
        pots = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && isStackSplashHealthPot(stack)) {
                pots += stack.stackSize;
            }
        }
        return pots;
    }

    public static boolean CurrentlyPotting() {
        return potting;
    }
}
