package com.github.boonx.sbabkodtest.buslines.trafiklabresponse;

public record TrafiklabResponse<T>(int StatusCode,
        String Message,
        int ExecutionTime,
        ResponseData<T> ResponseData) {
}
