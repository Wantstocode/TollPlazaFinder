package org.FreightFox.TollPlazaApplication.repository;

import java.util.List;
import org.FreightFox.TollPlazaApplication.model.TollPlaza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TollPlazaRepository extends JpaRepository<TollPlaza, Long> {

  @Query(
      value =
          "SELECT * FROM toll_plazas "
              + "WHERE latitude BETWEEN :latMin AND :latMax "
              + "AND longitude BETWEEN :lonMin AND :lonMax "
              + "ORDER BY latitude ASC "
              + "LIMIT 5",
      nativeQuery = true)
  List<TollPlaza> findTollPlazasInSegment(
      double latMin, double latMax, double lonMin, double lonMax);
}
