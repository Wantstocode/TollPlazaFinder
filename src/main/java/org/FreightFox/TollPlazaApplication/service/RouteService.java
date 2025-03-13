package org.FreightFox.TollPlazaApplication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import org.FreightFox.TollPlazaApplication.responseDTO.GeoPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RouteService {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${osrm.api.url}")
  private String OSRM_ROUTE_API;

  @Value("${nominatim.api.url}")
  private String nominatim_API_Url;

  public RouteService(RestTemplate restTemplate, ObjectMapper objectMapper) {
    this.restTemplate = restTemplate;
    this.objectMapper = objectMapper;
  }

  // Fetches waypoints along the route using OSRM and caches them in Redis.
  @Cacheable(
      value = "routeWaypoints",
      key =
          "#source.latitude + '_' + #source.longitude + '_' + #destination.latitude + '_' + #destination.longitude")
  public List<GeoPoint> getRouteWaypoints(GeoPoint source, GeoPoint destination) {

    System.out.println("Cache Miss: Fetching waypoints from OSRM API...");

    List<GeoPoint> waypoints = new ArrayList<>();
    String url =
        OSRM_ROUTE_API
            + source.getLongitude()
            + ","
            + source.getLatitude()
            + ";"
            + destination.getLongitude()
            + ","
            + destination.getLatitude()
            + "?overview=full&geometries=geojson";

    try {
      JsonNode response = restTemplate.getForObject(url, JsonNode.class);
      JsonNode coordinates = response.get("routes").get(0).get("geometry").get("coordinates");

      // Debug Log
      System.out.println(
          "DEBUG: Fetching waypoints between "
              + source.getLatitude()
              + ","
              + source.getLongitude()
              + " -> "
              + destination.getLatitude()
              + ","
              + destination.getLongitude());

      int waypointInterval =
          Math.max(1, coordinates.size() / 40);

      for (int i = 0; i < coordinates.size(); i += waypointInterval) {
        JsonNode coord = coordinates.get(i);
        double lon = coord.get(0).asDouble();
        double lat = coord.get(1).asDouble();
        waypoints.add(new GeoPoint(lat, lon));

        // Debug each waypoint
        System.out.println("DEBUG: Waypoint - Lat: " + lat + ", Lon: " + lon);
      }
    } catch (Exception e) {
      System.out.println("ERROR: Fetching waypoints failed: " + e.getMessage());
    }
    return waypoints;
  }


  // Fetches latitude and longitude for a given pincode using OpenStreetMap API and caches the result.
  @Cacheable(value = "pincodeCoordinates", key = "#pincode")
  public GeoPoint getCoordinatesFromPincode(String pincode) {
    try {
      // Use OpenStreetMap (Nominatim) and ensure results are limited to India
      String url = nominatim_API_Url + pincode;
      System.out.println("DEBUG: Fetching coordinates for pincode: " + pincode + " -> URL: " + url);

      JsonNode response = restTemplate.getForObject(url, JsonNode.class);
      if (response != null && response.isArray() && response.size() > 0) {
        JsonNode result = response.get(0);
        double latitude = result.get("lat").asDouble();
        double longitude = result.get("lon").asDouble();

        // Validate if coordinates are inside India
        if (latitude >= 6 && latitude <= 37 && longitude >= 68 && longitude <= 97) {
          System.out.println(
              "DEBUG: Pincode " + pincode + " -> Lat: " + latitude + ", Lon: " + longitude);
          return new GeoPoint(latitude, longitude);
        } else {
          System.out.println("ERROR: Pincode " + pincode + " returned an out-of-India location!");
          return null;
        }
      } else {
        System.out.println("ERROR: Empty response from Nominatim for pincode " + pincode);
      }
    } catch (Exception e) {
      System.out.println(
          "ERROR: Failed to fetch coordinates for " + pincode + ": " + e.getMessage());
    }
    return null;
  }

  // Calculates approximate distance between two coordinates using the Haversine formula.
  public double calculateDistance(GeoPoint point1, GeoPoint point2) {
    final int R = 6371; // Earth's radius in kilometers

    double lat1 = Math.toRadians(point1.getLatitude());
    double lon1 = Math.toRadians(point1.getLongitude());
    double lat2 = Math.toRadians(point2.getLatitude());
    double lon2 = Math.toRadians(point2.getLongitude());

    double dlat = lat2 - lat1;
    double dlon = lon2 - lon1;

    double a =
        Math.sin(dlat / 2) * Math.sin(dlat / 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    double distance = R * c; // Distance in km

    System.out.println(
        "DEBUG: Distance from "
            + point1.getLatitude()
            + ","
            + point1.getLongitude()
            + " -> "
            + point2.getLatitude()
            + ","
            + point2.getLongitude()
            + " = "
            + distance
            + " km");

    return distance;
  }
}
