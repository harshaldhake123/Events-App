package com.example.clubapplication;

import android.net.Uri;

public class Event {
    public String date, details, eventName, venue, organizer;
    public String image = "NULL";

    public Event(String eventName, String venue, String date, String organizer, String details) {
        this.date = date;
        this.details = details;
        this.eventName = eventName;
        this.organizer = organizer;
        this.venue = venue;
    }

    public Event(String date, String details, String eventName, String venue, String organizer, String image) {
        this.date = date;
        this.details = details;
        this.eventName = eventName;
        this.venue = venue;
        this.organizer = organizer;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Event() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String event_name) {
        this.eventName = event_name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }
}