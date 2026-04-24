package com.state.drone;

import java.util.Objects;

/**
 * Context object for the State pattern: holds current {@link FlightState} and delegates events.
 */
public final class FlightContext {

    private FlightState state;

    public FlightContext() {
        this.state = GroundState.INSTANCE;
    }

    public FlightState getState() {
        return state;
    }

    public FlightActionResult handle(FlightEvent event) {
        Objects.requireNonNull(event, "event");
        return state.handle(this, event);
    }

    void transitionTo(FlightState newState) {
        Objects.requireNonNull(newState, "newState");
        this.state = newState;
    }
}
