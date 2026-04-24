package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class EmergencyState implements FlightState {

    public static final EmergencyState INSTANCE = new EmergencyState();

    private EmergencyState() {
    }

    @Override
    public String id() {
        return "EMERGENCY";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case LAND_COMPLETE -> {
                context.transitionTo(GroundState.INSTANCE);
                yield FlightActionResult.ok("EMERGENCY_RESOLVED_ON_GROUND");
            }
            default -> FlightActionResult.reject("INVALID_TRANSITION");
        };
    }

    @Override
    public Set<FlightEvent> allowedEvents() {
        return EnumSet.of(FlightEvent.LAND_COMPLETE);
    }
}
