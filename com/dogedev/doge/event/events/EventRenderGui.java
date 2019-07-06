package com.dogedev.doge.event.events;

import com.dogedev.doge.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGui extends Event {
    private ScaledResolution resolution;

    public EventRenderGui(ScaledResolution resolution) {
        this.resolution = resolution;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
