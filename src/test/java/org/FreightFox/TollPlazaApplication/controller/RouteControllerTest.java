package org.FreightFox.TollPlazaApplication.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.FreightFox.TollPlazaApplication.requestDTO.RouteRequest;
import org.FreightFox.TollPlazaApplication.responseDTO.GeoPoint;
import org.FreightFox.TollPlazaApplication.responseDTO.RouteResponse;
import org.FreightFox.TollPlazaApplication.service.RouteService;
import org.FreightFox.TollPlazaApplication.service.TollPlazaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RouteController.class) // Ensures only controller is loaded
public class RouteControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private RouteService routeService;

  @MockBean private TollPlazaService tollPlazaService;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();
  }

  @Test
  void testGetTollPlazas_ValidRequest() throws Exception {
    RouteResponse mockResponse = new RouteResponse("110001", "560001", 1740.0, List.of());
    when(routeService.getCoordinatesFromPincode(anyString()))
        .thenReturn(new GeoPoint(28.6139, 77.2090));
    when(routeService.calculateDistance(any(), any())).thenReturn(1740.0);
    when(tollPlazaService.getTollPlazasBetweenPincodes(anyString(), anyString()))
        .thenReturn(List.of());

    mockMvc
        .perform(
            post("/api/v1/toll-plazas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RouteRequest("110001", "560001"))))
        .andExpect(status().isOk());
  }

  @Test
  void testGetTollPlazas_InvalidRequest() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/toll-plazas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RouteRequest("110001", "110001"))))
        .andExpect(status().isBadRequest());
  }
}
