package org.FreightFox.TollPlazaApplication.service;

import java.util.*;
import org.FreightFox.TollPlazaApplication.model.TollPlaza;
import org.FreightFox.TollPlazaApplication.repository.TollPlazaRepository;
import org.FreightFox.TollPlazaApplication.responseDTO.GeoPoint;
import org.FreightFox.TollPlazaApplication.responseDTO.TollPlazaResponse;
import org.springframework.stereotype.Service;

@Service
public class TollPlazaService {

  private final TollPlazaRepository tollPlazaRepository;
  private final RouteService routeService;

  public TollPlazaService(TollPlazaRepository tollPlazaRepository, RouteService routeService) {
    this.tollPlazaRepository = tollPlazaRepository;
    this.routeService = routeService;
  }

  // Finds toll plazas along the route using 50 km segment-based search
  public List<TollPlazaResponse> getTollPlazasBetweenPincodes(
      String sourcePincode, String destinationPincode) {

    // Convert pincodes to GeoPoints
    GeoPoint sourceGeoPoint = routeService.getCoordinatesFromPincode(sourcePincode);
    GeoPoint destGeoPoint = routeService.getCoordinatesFromPincode(destinationPincode);

    if (sourceGeoPoint == null || destGeoPoint == null) {
      throw new IllegalArgumentException("Invalid source or destination pincode");
    }

    // Get full route waypoints
    List<GeoPoint> routeWaypoints = routeService.getRouteWaypoints(sourceGeoPoint, destGeoPoint);

    // Break the route into segments (~50 km each)
    List<GeoPoint[]> routeSegments = getRouteSegments(routeWaypoints, 50);

    // Fetch toll plazas for each segment without allowing duplicates
    List<TollPlazaResponse> tollPlazas = new ArrayList<>();
    Set<String> addedTollPlazas = new HashSet<>(); // To avoid duplicate toll plazas

    for (GeoPoint[] segment : routeSegments) {
      GeoPoint segmentStart = segment[0];
      GeoPoint segmentEnd = segment[1];

      // Get all toll plazas in the segment bounding box
      List<TollPlaza> segmentTollPlazas =
          tollPlazaRepository.findTollPlazasInSegment(
              Math.min(segmentStart.getLatitude(), segmentEnd.getLatitude()),
              Math.max(segmentStart.getLatitude(), segmentEnd.getLatitude()),
              Math.min(segmentStart.getLongitude(), segmentEnd.getLongitude()),
              Math.max(segmentStart.getLongitude(), segmentEnd.getLongitude()));


      int count = 0;
      for (TollPlaza toll : segmentTollPlazas) {
        if (count >= 5) break; // Limit to 5 toll plazas per segment

        double distanceFromSource =
            routeService.calculateDistance(
                sourceGeoPoint, new GeoPoint(toll.getLatitude(), toll.getLongitude()));

        String uniqueKey = toll.getName() + "_" + toll.getLatitude() + "_" + toll.getLongitude();

        if (!addedTollPlazas.contains(uniqueKey)) {
          tollPlazas.add(
              new TollPlazaResponse(
                  toll.getName(), toll.getLatitude(), toll.getLongitude(), distanceFromSource));
          addedTollPlazas.add(uniqueKey);
          count++;
        }
      }
    }
    return tollPlazas;
  }

  // Breaks the full route into segments of approximately segmentDistance km each.
  public List<GeoPoint[]> getRouteSegments(List<GeoPoint> waypoints, double segmentDistance) {
    List<GeoPoint[]> segments = new ArrayList<>();

    if (waypoints.isEmpty()) return segments;

    GeoPoint prevPoint = waypoints.get(0);
    double accumulatedDistance = 0;

    for (int i = 1; i < waypoints.size(); i++) {
      GeoPoint currentPoint = waypoints.get(i);
      accumulatedDistance += routeService.calculateDistance(prevPoint, currentPoint);

      if (accumulatedDistance
          >= (segmentDistance - 5)) { // Overlapping segments for better coverage
        segments.add(new GeoPoint[] {prevPoint, currentPoint});
        prevPoint = currentPoint;
        accumulatedDistance = 0;
      }
    }

    // Always add the last segment if missing
    if (!segments.isEmpty()
        && segments.get(segments.size() - 1)[1] != waypoints.get(waypoints.size() - 1)) {
      segments.add(
          new GeoPoint[] {
            segments.get(segments.size() - 1)[1], waypoints.get(waypoints.size() - 1)
          });
    }
    return segments;
  }
}
