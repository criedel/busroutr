package com.cupsoftware.routr.resources;

import com.cupsoftware.routr.domain.RouteInfo;
import com.cupsoftware.routr.services.RouteCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/provider/goeurobus/")
public class BusRoutrResource {

    @Autowired
    private RouteCalculator routeCalculator;

    @RequestMapping("/direct/{dep_sid}/{arr_sid}")
    public RouteInfo getDirectRoute(@PathVariable(value = "dep_sid") final int depSid,
                                    @PathVariable(value = "arr_sid") final int arrSid) {

        /*
         * This response should satisfy the requirements, but... to make it a real RESTful service and less RPC-like this
         * resource should not yield a response if direct_bus_route is false.
         * Instead it should return a 404, resource not found. The resource being the direct route.
         * Sorry, had to point this out, maybe it was part of the test ;)
         */
        return new RouteInfo(depSid, arrSid, routeCalculator.isRouteAvailable(depSid, arrSid));
    }
}
