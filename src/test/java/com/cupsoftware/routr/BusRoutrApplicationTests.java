package com.cupsoftware.routr;

import com.cupsoftware.routr.services.RouteCalculator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BusRoutrApplicationTests {

    private RouteCalculator routeCalculator;

    @BeforeMethod
    public void setup() throws IOException {

        final Path tempFile = Files.createTempFile("busroutrexample", ".tmp");
        final List<String> lines = Arrays.asList("10",
                "1 153 150 148 106 17 20 160 140 24",
                "2 5 142 106 11",
                "19 153 121 114 150 5",
                "13 153 148 169 106 11 12",
                "14 114 150 142 12 179 174 17",
                "6 5 138 148 12 174 118 16 19 184",
                "7 121 114 150 5 148 169 11",
                "8 142 138 148 169 106 11 12",
                "18 153 114 5 138",
                "11 121 114 148 169 12 16 155");

        Files.write(tempFile, lines, Charset.forName("UTF-8"));

        this.routeCalculator = new RouteCalculator(tempFile.toFile().getAbsolutePath());
    }

    @Test
    public void calculate_route() {
        assertThat(this.routeCalculator.isRouteAvailable(153, 121), is(true));
        assertThat(this.routeCalculator.isRouteAvailable(5, 106), is(true));
        assertThat(this.routeCalculator.isRouteAvailable(169, 12), is(true));
        assertThat(this.routeCalculator.isRouteAvailable(150, 5), is(true));

        assertThat(this.routeCalculator.isRouteAvailable(1, 2), is(false));
        assertThat(this.routeCalculator.isRouteAvailable(150, 153), is(false));

    }

}
