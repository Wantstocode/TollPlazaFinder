package org.FreightFox.TollPlazaApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.FreightFox.TollPlazaApplication.responseDTO.GeoPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.client.RestTemplate;

public class RouteServiceTest {

  @Mock private RestTemplate restTemplate;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private RouteService routeService;

  private CacheManager cacheManager;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    cacheManager = new ConcurrentMapCacheManager("routeWaypoints", "pincodeCoordinates");
  }

  @Test
  void testCalculateDistance() {
    GeoPoint point1 = new GeoPoint(28.6139, 77.2090); // Delhi
    GeoPoint point2 = new GeoPoint(12.9716, 77.5946); // Bangalore

    double distance = routeService.calculateDistance(point1, point2);
    assertEquals(1740, distance, 100); // Allow 100km margin
  }

  @Test
  void testGetCoordinatesFromPincode() throws JsonProcessingException {
    String jsonResponse = "[{\"lat\": \"28.6139\", \"lon\": \"77.2090\"}]";
    when(restTemplate.getForObject(anyString(), eq(JsonNode.class)))
        .thenReturn(new ObjectMapper().readTree(jsonResponse));

    GeoPoint geoPoint = routeService.getCoordinatesFromPincode("110001");
    assertNotNull(geoPoint);
    assertEquals(28.6139, geoPoint.getLatitude());
    assertEquals(77.2090, geoPoint.getLongitude());
  }

  @Test
  void testGetRouteWaypoints() throws Exception {
    GeoPoint source = new GeoPoint(28.6139, 77.2090);
    GeoPoint destination = new GeoPoint(12.9716, 77.5946);

    String osrmResponse =
        "{ \"routes\": [{ \"geometry\": { \"coordinates\": [ [77.2090, 28.6139], [77.5, 28.7] ] } }] }";
    when(restTemplate.getForObject(anyString(), eq(JsonNode.class)))
        .thenReturn(new ObjectMapper().readTree(osrmResponse));

    List<GeoPoint> waypoints = routeService.getRouteWaypoints(source, destination);
    assertNotNull(waypoints);
    assertFalse(waypoints.isEmpty());
  }
}
