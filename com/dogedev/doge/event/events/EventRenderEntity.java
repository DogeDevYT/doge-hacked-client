package com.dogedev.doge.event.events;

import com.dogedev.doge.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity extends Event {
    private EntityLivingBase entity;
    private boolean pre;

    public EventRenderEntity(EntityLivingBase entity, boolean pre) {
        this.entity = entity;
        this.pre = pre;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public boolean isPre() {
        return this.pre;
    }

    public boolean isPost() {
        return !this.pre;
    }
}
