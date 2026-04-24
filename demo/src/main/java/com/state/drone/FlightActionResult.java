package com.state.drone;

/**
 * Outcome of handling a {@link FlightEvent}. {@code messageCode} is an English token for clients to map to UI strings.
 */
public record FlightActionResult(boolean success, String messageCode) {
    public static FlightActionResult ok(String messageCode) {
        return new FlightActionResult(true, messageCode);
    }

    public static FlightActionResult reject(String messageCode) {
        return new FlightActionResult(false, messageCode);
    }
}
