package com.krypt.Hubble.Users.Staff.Modal;

public class EventsModal {
    String event_title;
    String event_des;

    public EventsModal(String event_title, String event_des) {
        this.event_title = event_title;
        this.event_des = event_des;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_des() {
        return event_des;
    }

    public void setEvent_des(String event_des) {
        this.event_des = event_des;
    }
}
