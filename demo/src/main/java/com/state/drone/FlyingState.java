package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class FlyingState implements FlightState {

    public static final FlyingState INSTANCE = new FlyingState();

    private FlyingState() {
    }

    @Override
    public String id() {
        return "FLYING";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case REQUEST_RTL -> {
                context.transitionTo(RtlState.INSTANCE);
                yield FlightActionResult.ok("RTL_ENGAGED");
            }
            case LAND_COMPLETE -> {
                context.transitionTo(GroundState.INSTANCE);
                yield FlightActionResult.ok("LANDED_DISARMED");
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
        return EnumSet.of(FlightEvent.REQUEST_RTL, FlightEvent.LAND_COMPLETE, FlightEvent.EMERGENCY_TRIGGER);
    }
}
