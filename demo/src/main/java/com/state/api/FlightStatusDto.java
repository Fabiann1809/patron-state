package com.state.api;

import java.util.List;

public class FlightStatusDto {
    public String currentState;
    public List<String> allowedEvents;

    public FlightStatusDto() {
    }

    public FlightStatusDto(String currentState, List<String> allowedEvents) {
        this.currentState = currentState;
        this.allowedEvents = allowedEvents;
    }
}
