package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class RtlState implements FlightState {

    public static final RtlState INSTANCE = new RtlState();

    private RtlState() {
    }

    @Override
    public String id() {
        return "RTL";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case LAND_COMPLETE -> {
                context.transitionTo(GroundState.INSTANCE);
                yield FlightActionResult.ok("RTL_LANDED_DISARMED");
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
        return EnumSet.of(FlightEvent.LAND_COMPLETE, FlightEvent.EMERGENCY_TRIGGER);
    }
}
