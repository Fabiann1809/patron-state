package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class ArmedState implements FlightState {

    public static final ArmedState INSTANCE = new ArmedState();

    private ArmedState() {
    }

    @Override
    public String id() {
        return "ARMED";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case DISARM -> {
                context.transitionTo(GroundState.INSTANCE);
                yield FlightActionResult.ok("DISARMED");
            }
            case TAKEOFF -> {
                context.transitionTo(FlyingState.INSTANCE);
                yield FlightActionResult.ok("TAKEOFF_COMPLETE");
            }
            case EMERGENCY_TRIGGER -> {
                context.transitionTo(EmergencyState.INSTANCE);
                yield FlightActionResult.ok("EMERGENCY_ACTIVE");
            }
            default -> FlightActionResult.reject("INVALID_TRANSITION");
        };
    }

    @Override
    public Set<FlightEvent> allowedEvents() {
        return EnumSet.of(FlightEvent.DISARM, FlightEvent.TAKEOFF, FlightEvent.EMERGENCY_TRIGGER);
    }
}
