package com.github.boonx.sbabkodtest.buslines;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import com.github.boonx.sbabkodtest.buslines.dto.BusLine;
import com.github.boonx.sbabkodtest.buslines.dto.Stop;
import com.github.boonx.sbabkodtest.buslines.trafiklabresponse.JourneyPatternPointOnLineResponse;
import com.github.boonx.sbabkodtest.buslines.trafiklabresponse.LineResponse;
import com.github.boonx.sbabkodtest.buslines.trafiklabresponse.StopResponse;
import com.github.boonx.sbabkodtest.buslines.trafiklabresponse.TrafiklabResponse;

public class TrafiklabProcessor {

    private static final String API_BASE_URL = "https://api.sl.se/api2/LineData.json";

    private final List<LineResponse> lines;
    private final List<JourneyPatternPointOnLineResponse> journeyPatternPointOnLines;
    private final List<StopResponse> stops;

    public TrafiklabProcessor(String apiKey) {
        this.lines = getTrafikLabData("line", apiKey,
                new ParameterizedTypeReference<TrafiklabResponse<LineResponse>>() {});
        this.journeyPatternPointOnLines = getTrafikLabData("jour", apiKey,
                new ParameterizedTypeReference<TrafiklabResponse<JourneyPatternPointOnLineResponse>>() {});
        this.stops = getTrafikLabData("stop", apiKey,
                new ParameterizedTypeReference<TrafiklabResponse<StopResponse>>() {});
    }

    public List<BusLine> getBusLines() {
        // Spec says line numbers are unique, but "282" exists twice.
        Set<String> uniqueLineNumbers = new HashSet<>();
        return lines.stream()
                .filter((line) -> uniqueLineNumbers.add(line.LineNumber()))
                .map((line) -> new BusLine(line.LineNumber(), getStops(line.LineNumber())))
                .toList();
    }

    private List<Stop> getStops(String lineNumber) {
        List<StopResponse> stopResponses = journeyPatternPointOnLines.stream()
                .filter((journey) -> journey.DirectionCode().equals("1"))
                .filter((journey) -> journey.LineNumber().equals(lineNumber))
                .map(this::getStopResponse)
                // I chose to ignore stops missing in stops data.
                .map((optionalStop) -> optionalStop.orElse(null))
                .filter((nullableStop) -> nullableStop != null)
                .toList();

        return stopResponses.stream()
                .map(StopResponse::StopPointName)
                // It seems like a journey can contain multiple stops at the same station?
                // E.g. line 631 stops at both stop point numbers 60050 and 60054 (Norrtälje
                // station) in the same direction.
                .distinct()
                .map(Stop::new)
                .toList();
    }

    private Optional<StopResponse> getStopResponse(
            JourneyPatternPointOnLineResponse journeyPatternPointOnLineResponse) {
        return stops.stream()
                .filter((stop) -> stop.StopPointNumber()
                        .equals(journeyPatternPointOnLineResponse.JourneyPatternPointNumber()))
                .findFirst();
    }

    private static <T> List<T> getTrafikLabData(String model, String apiKey,
            ParameterizedTypeReference<TrafiklabResponse<T>> typeReference) {
        String uri = API_BASE_URL + "?model=" + model +
                "&DefaultTransportModeCode=BUS&key=" + apiKey;
        TrafiklabResponse<T> trafiklabResponse = RestClient.create().get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Accept-Encoding", "gzip, deflate")
                .retrieve()
                .onStatus((status) -> !status.is2xxSuccessful(), (request, response) -> {
                    throw new ExceptionInInitializerError("Unable to initialize server.");
                })
                .body(typeReference);

        if (trafiklabResponse == null) {
            throw new ExceptionInInitializerError("Unable to initialize server.");
        }

        if (trafiklabResponse.StatusCode() == 1006) {
            throw new ExceptionInInitializerError(
                    "Unable to initialize server. Too many API requests per minute.");
        }

        return trafiklabResponse != null ? Arrays.asList(trafiklabResponse.ResponseData().Result())
                : Collections.emptyList();
    }
}
