package com.cp.user.bus.event;

/**
 * Created by yi on 08/07/2017.
 */

public class DrawerMenuEvent {

    public static enum DrawerMenuEventType {
        REFRESH_HISTORY_EVENT
    }

    private DrawerMenuEvent.DrawerMenuEventType mEventType;

    public DrawerMenuEvent(DrawerMenuEvent.DrawerMenuEventType eventType) {
        this.mEventType = eventType;
    }

    public DrawerMenuEvent.DrawerMenuEventType getEventType() {
        return mEventType;
    }

}
