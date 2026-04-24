package com.state.drone;

import java.util.EnumSet;
import java.util.Set;

public final class PreflightChecksState implements FlightState {

    public static final PreflightChecksState INSTANCE = new PreflightChecksState();

    private PreflightChecksState() {
    }

    @Override
    public String id() {
        return "PREFLIGHT_CHECKS";
    }

    @Override
    public FlightActionResult handle(FlightContext context, FlightEvent event) {
        return switch (event) {
            case CHECKS_PASSED -> {
                context.transitionTo(ReadyToArmState.INSTANCE);
                yield FlightActionResult.ok("CHECKS_OK");
            }
            case CHECKS_FAILED -> {
                context.transitionTo(GroundState.INSTANCE);
                yield FlightActionResult.ok("RETURNED_TO_GROUND");
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
        return EnumSet.of(
                FlightEvent.CHECKS_PASSED,
                FlightEvent.CHECKS_FAILED,
                FlightEvent.EMERGENCY_TRIGGER
        );
    }
}
