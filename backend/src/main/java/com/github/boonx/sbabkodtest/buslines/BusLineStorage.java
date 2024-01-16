package com.github.boonx.sbabkodtest.buslines;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.github.boonx.sbabkodtest.buslines.dto.BusLine;
import jakarta.annotation.PostConstruct;

@Component
public class BusLineStorage {

    @Value("${api.key}")
    private String apiKey;

    private List<BusLine> busLines = new ArrayList<>();

    public List<BusLine> getBusLines() {
        return busLines;
    }

    @PostConstruct
    public void initBusLines() {
        this.busLines = new TrafiklabProcessor(apiKey).getBusLines();
    }
}
