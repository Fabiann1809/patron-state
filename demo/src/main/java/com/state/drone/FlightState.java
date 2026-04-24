package com.state.drone;

import java.util.Set;

/**
 * State in the State pattern: interprets {@link FlightEvent}s for the current flight phase.
 */
public interface FlightState {

    /** Stable English identifier for API and logs (e.g. GROUND). */
    String id();

    /**
     * Handles an event. On success, may call {@link FlightContext#transitionTo(FlightState)}.
     */
    FlightActionResult handle(FlightContext context, FlightEvent event);

    /** Events the UI may offer; invalid combinations still return {@link FlightActionResult#reject}. */
    Set<FlightEvent> allowedEvents();
}
