package com.github.boonx.sbabkodtest.buslines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.github.boonx.sbabkodtest.buslines.dto.BusLine;

@Service
public class BusLinesService {

    private final BusLineStorage busLineStorage;

    public BusLinesService(BusLineStorage busLineStorage) {
        this.busLineStorage = busLineStorage;
    }

    public List<BusLine> getTopTenBusLines() {
        List<BusLine> sortedBusLines = busLineStorage.getBusLines().stream()
                .sorted((busLine1, busLine2) -> Integer.compare(busLine1.stops().size(),
                        busLine2.stops().size()))
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.reverse(sortedBusLines);

        return sortedBusLines.subList(0,
                sortedBusLines.size() < 10 ? sortedBusLines.size() : 10);
    }
}
