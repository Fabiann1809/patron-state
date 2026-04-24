package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class GroundState implements FlightState {

    public static final GroundState INSTANCE = new GroundState();

    private GroundState() {
    }

    @Override
    public String id() {
        return "GROUND";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case START_CHECKS -> {
                context.transitionTo(PreflightChecksState.INSTANCE);
                yield FlightActionResult.ok("CHECKS_STARTED");
            }
            default -> FlightActionResult.reject("INVALID_TRANSITION");
        };
    }

    @Override
    public Set<FlightEvent> allowedEvents() {
        return EnumSet.of(FlightEvent.START_CHECKS);
    }
}
