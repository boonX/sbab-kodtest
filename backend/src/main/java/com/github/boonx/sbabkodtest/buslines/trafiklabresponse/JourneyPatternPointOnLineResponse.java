package com.github.boonx.sbabkodtest.buslines.trafiklabresponse;

public record JourneyPatternPointOnLineResponse(String LineNumber,
                String DirectionCode,
                String JourneyPatternPointNumber,
                String LastModifiedUtcDateTime,
                String ExistsFromDate) {
}
