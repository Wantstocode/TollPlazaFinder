package org.FreightFox.TollPlazaApplication.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "toll_plazas")
public class TollPlaza {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Double latitude;

  @Column(nullable = false)
  private Double longitude;

  @Column(nullable = false)
  private String geoState;

  public TollPlaza(String name, double latitude, double longitude, String geoState) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
    this.geoState = geoState;
  }
}
