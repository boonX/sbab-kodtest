package com.github.boonx.sbabkodtest.buslines.trafiklabresponse;

public record StopResponse(String StopPointNumber,
        String StopPointName,
        String StopAreaNumber,
        String LocationNorthingCoordinate,
        String LocationEastingCoordinate,
        String ZoneShortName,
        String StopAreaTypeCode,
        String LastModifiedUtcDateTime,
        String ExistsFromDate) {
}
