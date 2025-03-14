package com.onedream.event_livedata;


public class EventLiveData<T> extends ProtectedEventLiveData<T> {

    public EventLiveData(T value) {
        super(value);
    }

    public EventLiveData() {
        super();
    }

}
