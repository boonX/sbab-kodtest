package com.github.boonx.sbabkodtest.buslines.dto;

import java.util.List;

public record BusLine(String lineNumber, List<Stop> stops) {

}
