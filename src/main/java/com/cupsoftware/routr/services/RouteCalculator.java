package com.cupsoftware.routr.services;

import com.cupsoftware.routr.domain.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RouteCalculator {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private List<Optional<Route>> routes;

    public RouteCalculator(@Value("${data.file}") final String dataFile) throws IOException {

        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath(dataFile))) {

            // maps all lines, skipping the info line, trimming accidental whitespace and maps to Route objects
            this.routes = lines.skip(1).map(String::trim).map(this::parseRoute).collect(Collectors.toList());
        }
    }

    /**
     * Takes two station ids and checks if there is a route in that direction.
     * I opted against arbitrary directions in the route because routes may contain stations only in one direction
     * (in reality). The requirement wasn’t specific enough here.
     *
     * @param depSid Departure Station ID
     * @param arrSid Arrival Station ID
     * @return true in case the trip is available, false otherwise
     */
    public boolean isRouteAvailable(final int depSid, final int arrSid) {

        for (Optional<Route> route : routes) {
            if (!route.isPresent()) {
                continue;
            }
            if (isJourneyInRoute(depSid, arrSid, route.get())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Parses a line of the input file and transforms it into a Route if it matches the format.
     *
     * @param line
     * @return an optional of a Route if the mapping was successful, 'empty' otherwise.
     */
    private Optional<Route> parseRoute(final String line) {

        // The first token is the routeId. We don’t need it but I'll read it for validation.
        final String[] rawRoute = line.split(" ", 2);
        final String rawRouteId = rawRoute[0];
        if (!rawRouteId.matches("\\d+")) {
            log.warn("Route’s first token '{}' does not comply with the required format.", rawRouteId);
            return Optional.empty();
        }

        final int routeId = Integer.parseInt(rawRouteId);
        final List<Integer> stationIds = Arrays.stream(rawRoute[1].split(" ")).filter(sid -> sid.matches("\\d+")).map(Integer::parseInt).collect(Collectors.toList());

        if (stationIds.size() < 2) {
            /*
             * Although the requirement states "This list contains at least two integers." I guess it’s ok to assume
             * that a route with only one station (since the other integer is the route id...) makes no sense.
             * Maybe for city-circle tours but even they have stations of interest.
             */
            log.warn("Invalid route: {} station ids are too few!", stationIds.size());
            return Optional.empty();
        }

        return Optional.of(new Route(routeId, stationIds));
    }

    /**
     * Checks if departure and arrival stations are present in the route’s list of stations.
     *
     * @param depSid Departure Station ID
     * @param arrSid Arrival Station ID
     * @param route
     * @return true if the stationIds contain depSid and arrSid in that order, false otherwise.
     */
    private Boolean isJourneyInRoute(final int depSid, final int arrSid, final Route route) {

        boolean depSidFound = false;

        final List<Integer> stationIds = route.getStationIds();

        for (Integer sid : stationIds) {
            if (sid.equals(depSid)) {
                depSidFound = true;
            } else if (depSidFound && sid.equals(arrSid)) {
                return true;
            }
        }

        return false;
    }
}
