package com.cupsoftware.routr.domain;

import lombok.Data;

import java.util.List;

@Data
public class Route {

    private final int routeId;

    private final List<Integer> stationIds;

    public Route(final int routeId, final List<Integer> stationIds) {
        this.routeId = routeId;
        this.stationIds = stationIds;
    }
}
