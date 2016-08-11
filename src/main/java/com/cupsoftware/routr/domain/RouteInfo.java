package com.cupsoftware.routr.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RouteInfo {

    private final int depSid;

    private final int arrSid;

    private final boolean directBusRoute;

    @JsonCreator
    public RouteInfo(@JsonProperty("dep_sid") final int depSid,
                     @JsonProperty("arr_sid") final int arrSid,
                     @JsonProperty("direct_bus_route") final boolean directBusRoute) {
        super();
        this.depSid = depSid;
        this.arrSid = arrSid;
        this.directBusRoute = directBusRoute;
    }

}
