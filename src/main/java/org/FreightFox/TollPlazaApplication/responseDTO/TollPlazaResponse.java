package org.FreightFox.TollPlazaApplication.responseDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TollPlazaResponse {
  private String name;
  private Double latitude;
  private Double longitude;
  private Double distanceFromSource;
}
