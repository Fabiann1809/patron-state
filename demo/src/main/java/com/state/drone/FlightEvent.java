package com.state.drone;

/**
 * High-level commands or system events for the mission state machine.
 */
public enum FlightEvent {
    START_CHECKS,
    CHECKS_PASSED,
    CHECKS_FAILED,
    ARM,
    DISARM,
    TAKEOFF,
    REQUEST_RTL,
    LAND_COMPLETE,
    EMERGENCY_TRIGGER
}
