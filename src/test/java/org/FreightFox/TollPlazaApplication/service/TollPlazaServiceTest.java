package org.FreightFox.TollPlazaApplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.FreightFox.TollPlazaApplication.repository.TollPlazaRepository;
import org.FreightFox.TollPlazaApplication.responseDTO.GeoPoint;
import org.FreightFox.TollPlazaApplication.responseDTO.TollPlazaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;

public class TollPlazaServiceTest {

  @Mock private TollPlazaRepository tollPlazaRepository;

  @Mock private RouteService routeService;

  private CacheManager cacheManager;

  @InjectMocks private TollPlazaService tollPlazaService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetTollPlazasBetweenPincodes_NoTollPlazasFound() {

    GeoPoint sourceGeoPoint = new GeoPoint(28.6139, 77.2090);
    GeoPoint destGeoPoint = new GeoPoint(12.9716, 77.5946);

    when(routeService.getCoordinatesFromPincode("110001")).thenReturn(sourceGeoPoint);
    when(routeService.getCoordinatesFromPincode("560001")).thenReturn(destGeoPoint);

    List<GeoPoint> routeWaypoints =
        Arrays.asList(
            new GeoPoint(28.6139, 77.2090),
            new GeoPoint(27.2, 79.3),
            new GeoPoint(12.9716, 77.5946));
    when(routeService.getRouteWaypoints(any(), any())).thenReturn(routeWaypoints);

    when(tollPlazaRepository.findTollPlazasInSegment(
            anyDouble(), anyDouble(), anyDouble(), anyDouble()))
        .thenReturn(List.of());

    List<TollPlazaResponse> result =
        tollPlazaService.getTollPlazasBetweenPincodes("110001", "560001");

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testGetRouteSegments_InsufficientWaypoints() {

    List<GeoPoint> waypoints = List.of(new GeoPoint(28.6139, 77.2090)); // Delhi

    List<GeoPoint[]> segments = tollPlazaService.getRouteSegments(waypoints, 50);

    assertNotNull(segments);
    assertTrue(segments.isEmpty());
  }
}
