package org.FreightFox.TollPlazaApplication.responseDTO;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoPoint implements Serializable {
  private double latitude;
  private double longitude;

}
