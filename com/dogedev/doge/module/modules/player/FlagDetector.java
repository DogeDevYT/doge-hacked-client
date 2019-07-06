/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dogedev.doge.module.modules.player;

import com.dogedev.doge.event.EventTarget;
import com.dogedev.doge.event.events.EventPostMotionUpdate;
import com.dogedev.doge.event.events.EventReceivePacket;
import com.dogedev.doge.module.Category;
import com.dogedev.doge.module.Module;
import com.dogedev.doge.utils.ChatUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class FlagDetector extends Module {
    private List<Vec3> lastLocations = new ArrayList<>();
    private List<Long> lastSetBacks = new ArrayList<>();

    public FlagDetector() {
        super("FlagDetector", Keyboard.KEY_NONE, Category.PLAYER);
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        List<Long> remove = new ArrayList<>();

        for (Long lastSetBack : lastSetBacks) {
            if (System.currentTimeMillis() - lastSetBack > 5000) {
                remove.add(lastSetBack);
            }
        }
        for (Long aLong : remove) {
            lastSetBacks.remove(aLong);
        }

        lastLocations.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));

        while (lastLocations.size() > 30) {
            lastLocations.remove(0);
        }
    }

    @EventTarget
    public void onPacketRecieve(EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook p = (S08PacketPlayerPosLook) event.getPacket();
            boolean setback = lastLocations.stream().anyMatch(loc -> p.getX() == loc.xCoord && p.getY() == loc.yCoord && p.getZ() == loc.zCoord);

            if (setback) {
                lastSetBacks.add(System.currentTimeMillis());
                if (lastSetBacks.size() < 3)
                    ChatUtil.info(EnumChatFormatting.RED + " Flag Detected");
            }
        }
    }
}
