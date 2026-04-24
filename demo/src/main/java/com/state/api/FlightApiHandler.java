package com.state.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.state.drone.FlightActionResult;
import com.state.drone.FlightContext;
import com.state.drone.FlightEvent;
import com.state.drone.FlightState;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-style handler for flight mission state (State pattern backend).
 */
public final class FlightApiHandler implements HttpHandler {

    private static final Gson GSON = new Gson();

    private final FlightContext flightContext;

    public FlightApiHandler(FlightContext flightContext) {
        this.flightContext = flightContext;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(method)) {
            handleGet(exchange);
            return;
        }
        if ("OPTIONS".equalsIgnoreCase(method)) {
            sendJson(exchange, 204, "");
            return;
        }
        if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
            return;
        }
        sendJson(exchange, 405, "{\"error\":\"METHOD_NOT_ALLOWED\"}");
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        FlightStatusDto body = buildStatus();
        sendJson(exchange, 200, GSON.toJson(body));
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String raw = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        FlightActionRequestDto request;
        try {
            request = GSON.fromJson(raw, FlightActionRequestDto.class);
        } catch (JsonSyntaxException e) {
            sendJson(exchange, 400, "{\"error\":\"INVALID_JSON\"}");
            return;
        }
        if (request == null || request.event == null || request.event.isBlank()) {
            sendJson(exchange, 400, "{\"error\":\"MISSING_EVENT\"}");
            return;
        }
        FlightEvent event;
        try {
            event = FlightEvent.valueOf(request.event.trim());
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, "{\"error\":\"UNKNOWN_EVENT\"}");
            return;
        }

        FlightActionResult result;
        synchronized (flightContext) {
            result = flightContext.handle(event);
        }

        FlightStatusDto status = buildStatus();
        FlightActionResponseDto response = new FlightActionResponseDto(
                status.currentState,
                status.allowedEvents,
                result.success(),
                result.messageCode()
        );
        int code = result.success() ? 200 : 409;
        sendJson(exchange, code, GSON.toJson(response));
    }

    private FlightStatusDto buildStatus() {
        FlightState state;
        synchronized (flightContext) {
            state = flightContext.getState();
        }
        List<String> events = state.allowedEvents().stream()
                .map(Enum::name)
                .collect(Collectors.toCollection(ArrayList::new));
        return new FlightStatusDto(state.id(), events);
    }

    private static void sendJson(HttpExchange exchange, int status, String jsonBody) throws IOException {
        byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        if (status == 204) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
