package com.dogedev.doge.module.modules.combat;

import com.dogedev.doge.Doge;
import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPreMotionUpdate;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.RenderUtils;
import com.dogedev.doge.utils.RotationUtils;
import de.Hero.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BowAimbot extends Module {
    @Override
    public void setup() {
        Doge.instance.settingsManager.rSetting(new Setting("AimPlayers", this, true));
        Doge.instance.settingsManager.rSetting(new Setting("AimAnimals", this, false));
        Doge.instance.settingsManager.rSetting(new Setting("AimMonsters", this, true));
        Doge.instance.settingsManager.rSetting(new Setting("AimVillagers", this, false));
        Doge.instance.settingsManager.rSetting(new Setting("AimTeammates", this, false));
        Doge.instance.settingsManager.rSetting(new Setting("AimInvisibles", this, false));
    }

    public BowAimbot() {
        super("BowAimbot", Keyboard.KEY_NONE, Category.COMBAT);
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (isToggled()) {
            EntityLivingBase target = getTarget();
            if ((this.mc.thePlayer.isUsingItem()) && ((this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow))) {
                if (target != this.mc.thePlayer && target != null) {
                    float[] rotations = RotationUtils.getBowAngles(target);
                    mc.thePlayer.rotationYaw = rotations[0];
                    mc.thePlayer.rotationPitch = rotations[1];
                }
            }
        }
    }

    private EntityLivingBase getTarget() {
        List<EntityLivingBase> loaded = new ArrayList();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                if (ent instanceof EntityPlayer && Doge.instance.settingsManager.getSettingByName("AimPlayers").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
                if (ent instanceof EntityAnimal && Doge.instance.settingsManager.getSettingByName("AimAnimals").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
                if (ent instanceof EntityMob && Doge.instance.settingsManager.getSettingByName("AimMonsters").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
                if (ent instanceof EntityVillager && Doge.instance.settingsManager.getSettingByName("AimVillagers").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
                if (ent instanceof EntityVillager && Doge.instance.settingsManager.getSettingByName("AimVillagers").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
                if (ent.isOnSameTeam(mc.thePlayer) && Doge.instance.settingsManager.getSettingByName("AimTeammates").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
                if (ent.isInvisible() && Doge.instance.settingsManager.getSettingByName("AimInvisibles").getValBoolean() && mc.thePlayer.canEntityBeSeen(ent) && mc.thePlayer.getDistanceToEntity(ent) < 15) {
                    loaded.add(ent);
                }
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        loaded.sort((o1, o2) -> {
            float[] rot1 = RotationUtils.getRotations(o1);
            float[] rot2 = RotationUtils.getRotations(o2);
            return (int) ((RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot1[0])
                    + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot1[1]))
                    - (RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot2[0])
                    + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot2[1])));
        });
        EntityLivingBase target = loaded.get(0);
        return target;
    }

}
