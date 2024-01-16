package com.github.boonx.sbabkodtest.buslines;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.boonx.sbabkodtest.buslines.dto.BusLine;

@RestController
@RequestMapping("/buslines")
@CrossOrigin(origins = "http://localhost:3000")
public class BusLinesController {

    private final BusLinesService busLinesService;

    public BusLinesController(BusLinesService busLinesService) {
        this.busLinesService = busLinesService;
    }

    @GetMapping
    public List<BusLine> getBuslines() {
        return busLinesService.getTopTenBusLines();
    }
}
