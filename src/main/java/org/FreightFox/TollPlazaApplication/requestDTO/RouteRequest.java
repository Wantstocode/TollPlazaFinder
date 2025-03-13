package org.FreightFox.TollPlazaApplication.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest{

  @NotBlank(message = "Source pincode is required")
  private String sourcePincode;

  @NotBlank(message = "Destination pincode is required")
  private String destinationPincode;
}
