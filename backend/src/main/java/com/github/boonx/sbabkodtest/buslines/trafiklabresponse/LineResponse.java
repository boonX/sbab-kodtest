package com.github.boonx.sbabkodtest.buslines.trafiklabresponse;

public record LineResponse(String LineNumber,
                String LineDesignation,
                String DefaultTransportMode,
                String DefaultTransportModeCode,
                String LastModifiedUtcDateTime,
                String ExistsFromDate) {
}
