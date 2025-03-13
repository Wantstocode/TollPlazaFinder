package org.FreightFox.TollPlazaApplication.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.FreightFox.TollPlazaApplication.requestDTO.RouteRequest;
import org.FreightFox.TollPlazaApplication.responseDTO.ErrorResponse;
import org.FreightFox.TollPlazaApplication.responseDTO.RouteResponse;
import org.FreightFox.TollPlazaApplication.responseDTO.TollPlazaResponse;
import org.FreightFox.TollPlazaApplication.service.RouteService;
import org.FreightFox.TollPlazaApplication.service.TollPlazaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/toll-plazas")
public class RouteController {

  private final RouteService routeService;
  private final TollPlazaService tollPlazaService;

  public RouteController(RouteService routeService, TollPlazaService tollPlazaService) {
    this.routeService = routeService;
    this.tollPlazaService = tollPlazaService;
  }

  //  API to fetch toll plazas between two pincodes
  @PostMapping
  public ResponseEntity<?> getTollPlazas(@Valid @RequestBody RouteRequest request) {
    try {

      System.out.println("DEBUG: Received request: " + request);

      // Validate if source & destination are the same
      if (request.getSourcePincode().equals(request.getDestinationPincode())) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("Source and destination pincodes cannot be the same"));
      }

      // Convert pincodes to GeoPoints
      var sourceGeoPoint = routeService.getCoordinatesFromPincode(request.getSourcePincode());
      var destGeoPoint = routeService.getCoordinatesFromPincode(request.getDestinationPincode());

      if (sourceGeoPoint == null || destGeoPoint == null) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse("Invalid source or destination pincode"));
      }

      // Get total route distance
      double distanceInKm = routeService.calculateDistance(sourceGeoPoint, destGeoPoint);

      // Fetch toll plazas using TollPlazaService
      List<TollPlazaResponse> tollPlazas =
          tollPlazaService.getTollPlazasBetweenPincodes(
              request.getSourcePincode(), request.getDestinationPincode());

      // Create structured response
      RouteResponse response =
          new RouteResponse(
              request.getSourcePincode(),
              request.getDestinationPincode(),
              distanceInKm,
              tollPlazas);
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(new ErrorResponse("An unexpected error occurred: " + e.getMessage()));
    }
  }
}
