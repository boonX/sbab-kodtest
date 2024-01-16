package com.github.boonx.sbabkodtest.buslines;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.boonx.sbabkodtest.buslines.dto.BusLine;
import com.github.boonx.sbabkodtest.buslines.dto.Stop;

@SpringBootTest
@AutoConfigureMockMvc
public class BusLinesControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BusLineStorage busLineStorage;

    @Test
    public void getsBusLines() throws Exception {
        var busLines = List.of(new BusLine("1", List.of(new Stop("Malmö C"))));

        Mockito.when(busLineStorage.getBusLines()).thenReturn(busLines);

        mvc.perform(MockMvcRequestBuilders.get("/buslines").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    public void getsSortedBusLines() throws Exception {
        var busLines = List.of(createBusLine("1", 1),
                createBusLine("3", 3),
                createBusLine("2", 2));

        Mockito.when(busLineStorage.getBusLines()).thenReturn(busLines);

        mvc.perform(MockMvcRequestBuilders.get("/buslines").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("[0].lineNumber").value("3"))
                .andExpect(jsonPath("[1].lineNumber").value("2"))
                .andExpect(jsonPath("[2].lineNumber").value("1"));
    }

    @Test
    public void getsAtMostTenSortedBusLines() throws Exception {
        var busLines = Collections.nCopies(11, createBusLine("1", 1));

        Mockito.when(busLineStorage.getBusLines()).thenReturn(busLines);

        mvc.perform(MockMvcRequestBuilders.get("/buslines").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(10)));
    }

    private BusLine createBusLine(String lineNumber, int nbrOfStops) {
        return new BusLine(lineNumber, Collections.nCopies(nbrOfStops, new Stop("Some station")));
    }
}
