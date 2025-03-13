package org.FreightFox.TollPlazaApplication.responseDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
  private String sourcePincode;
  private String destinationPincode;
  private Double distanceInKm;
  private List<TollPlazaResponse> tollPlazas;

  public String toJson() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }
}
