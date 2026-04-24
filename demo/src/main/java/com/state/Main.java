package com.state;

import com.state.api.FlightApiHandler;
import com.state.api.StaticResourceHandler;
import com.state.drone.FlightContext;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public final class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        FlightContext flightContext = new FlightContext();
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/api/flight", new FlightApiHandler(flightContext));
        server.createContext("/", new StaticResourceHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Drone mission demo: http://localhost:" + port + "/");
    }
}
