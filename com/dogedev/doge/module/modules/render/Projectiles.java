package com.dogedev.doge.module.modules.render;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.Event3D;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import net.minecraft.item.*;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Projectiles extends Module {
    public Projectiles() {
        super("Projectiles", Keyboard.KEY_NONE, Category.RENDER);
    }

    @EventTarget
    public void onRender(Event3D event) {
        if (mc.thePlayer.getCurrentEquippedItem() != null && isThrowable(mc.thePlayer.getCurrentEquippedItem().getItem())) {

            double x = mc.thePlayer.lastTickPosX
                    + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX)
                    * (double) mc.timer.renderPartialTicks
                    - (double) (MathHelper.cos((float) Math.toRadians((double) mc.thePlayer.rotationYaw)) * 0.16F);

            double y = mc.thePlayer.lastTickPosY
                    + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double) mc.timer.renderPartialTicks
                    + (double) mc.thePlayer.getEyeHeight() - 0.100149011612D;

            double z = mc.thePlayer.lastTickPosZ
                    + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ)
                    * (double) mc.timer.renderPartialTicks
                    - (double) (MathHelper.sin((float) Math.toRadians((double) mc.thePlayer.rotationYaw)) * 0.16F);

            double motionX, motionY, motionZ;
            int vertexCounter = 0;

            float con;

            if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                con = 1f;
            }else{
                con = 0.4f;
            }

            motionX = (-MathHelper.sin((float) Math.toRadians(mc.thePlayer.rotationYaw)) * MathHelper.cos((float) Math.toRadians(mc.thePlayer.rotationPitch)) * con);
            motionZ = (MathHelper.cos((float) Math.toRadians(mc.thePlayer.rotationYaw)) * MathHelper.cos((float) Math.toRadians(mc.thePlayer.rotationPitch)) * con);
            motionY = (-MathHelper.sin((float) Math.toRadians(mc.thePlayer.rotationPitch)) * con);

            double ssum = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
            motionX /= ssum;
            motionY /= ssum;
            motionZ /= ssum;

            if (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {

                float pow = (float) (72000 - mc.thePlayer.getItemInUseCount()) / 20.0F;
                pow = (pow * pow + pow * 2.0F) / 3.0F;

                if (pow > 1.0F) {
                    pow = 1.0F;
                }

                if (pow <= 0.1F) {
                    pow = 1.0F;
                }

                pow *= 2.0F;
                pow *= 1.5F;

                motionX *= pow;
                motionY *= pow;
                motionZ *= pow;

            }else{

                motionX *= 1.5d;
                motionY *= 1.5d;
                motionZ *= 1.5d;

            }

            GL11.glPushMatrix();

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(3042);
            GL11.glDisable(3553);

            GL11.glLineWidth(3.5F);
            GL11.glColor4f(1f, 0f, 0, 0.5f);
            GL11.glBegin(GL11.GL_LINE_STRIP);

            while(vertexCounter++ < 200) {

                GL11.glVertex3d(x * 1.0D - mc.getRenderManager().renderPosX, y * 1.0D - mc.getRenderManager().renderPosY, z * 1.0D - mc.getRenderManager().renderPosZ);

                x += motionX;
                y += motionY;
                z += motionZ;

                motionX *= 0.99D;
                motionY *= 0.99D;
                motionZ *= 0.99D;
                motionY -= 0.05D;

                if(mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY+mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(x, y, z), true) != null){
                    break;
                }
            }

            GL11.glEnd();

            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glEnable(GL11.GL_LIGHTING);

            GL11.glPopMatrix();
        }
    }

    private boolean isThrowable(Item item) {
        return item instanceof ItemBow || item instanceof ItemSnowball
                || item instanceof ItemEgg || item instanceof ItemEnderPearl;
    }
}
