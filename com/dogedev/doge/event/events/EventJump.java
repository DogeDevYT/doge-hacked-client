package com.dogedev.doge.event.events;

import com.dogedev.doge.event.Event;

public class EventJump extends Event {
    private double motionY;
    private boolean pre;

    public EventJump(double motionY, boolean pre) {
        this.motionY = motionY;
        this.pre = pre;
    }

    public double getMotionY() { return this.motionY; }


    public void setMotionY(double motiony) { this.motionY = motiony; }



    public boolean isPre() { return this.pre; }



    public boolean isPost() { return !this.pre; }
}
