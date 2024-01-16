package com.github.boonx.sbabkodtest.buslines.trafiklabresponse;

public record ResponseData<T>(String Version, String Type, T[] Result) {
}
