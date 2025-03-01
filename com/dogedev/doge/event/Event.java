package com.dogedev.doge.event;

import com.dogedev.doge.Doge;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {
    private boolean cancelled;

    public enum State {
        PRE("PRE", 0), POST("POST", 1);
        State(String string, int number) {
        }
    }

    public Event call() {
        this.cancelled = false;
        this.call(this);
        return this;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }

    private static void call(Event event) {
        ArrayHelper<Data> dataList = Doge.instance.eventManager.get(event.getClass());
        if (dataList != null) {
            for (Data data : dataList) {
                try {
                    data.target.invoke(data.source, event);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
