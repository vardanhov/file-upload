package com.example.uploadfile.logger;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


public enum Markers {
    ADMIN("admin"),
    USER("user");

    private Marker marker;

    Markers(String marker) {
        this.marker = MarkerFactory.getMarker(marker);
    }

    public Marker getMarker() {
        return marker;
    }

    @Override
    public String toString() {
        return "Markers{" + "marker=" + marker + '}';
    }
}
