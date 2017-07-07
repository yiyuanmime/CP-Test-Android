package com.cp.user.bus.event;

/**
 * Created by yi on 07/07/2017.
 */

public class LocationEvent {

    public static enum LocationEventType {
        NEW_LOCATION_FOUND
    }

    private LocationEventType mEventType;

    public LocationEvent(LocationEventType eventType) {
        this.mEventType = eventType;
    }

    public LocationEventType getEventType() {
        return mEventType;
    }

}
