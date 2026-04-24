package com.state.api;

import java.util.List;

public final class FlightActionResponseDto extends FlightStatusDto {
    public boolean lastSuccess;
    public String lastMessageCode;

    public FlightActionResponseDto() {
    }

    public FlightActionResponseDto(
            String currentState,
            List<String> allowedEvents,
            boolean lastSuccess,
            String lastMessageCode
    ) {
        super(currentState, allowedEvents);
        this.lastSuccess = lastSuccess;
        this.lastMessageCode = lastMessageCode;
    }
}
