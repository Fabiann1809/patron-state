package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class ReadyToArmState implements FlightState {

    public static final ReadyToArmState INSTANCE = new ReadyToArmState();

    private ReadyToArmState() {
    }

    @Override
    public String id() {
        return "READY_TO_ARM";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case ARM -> {
                context.transitionTo(ArmedState.INSTANCE);
                yield FlightActionResult.ok("ARMED");
            }
            case EMERGENCY_TRIGGER -> {
                context.transitionTo(GroundState.INSTANCE);
                yield FlightActionResult.ok("PREFLIGHT_ABORTED");
            }
            default -> FlightActionResult.reject("INVALID_TRANSITION");
        };
    }

    @Override
    public Set<FlightEvent> allowedEvents() {
        return EnumSet.of(FlightEvent.ARM, FlightEvent.EMERGENCY_TRIGGER);
    }
}
