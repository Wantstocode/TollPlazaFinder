package org.FreightFox.TollPlazaApplication.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.FreightFox.TollPlazaApplication.model.TollPlaza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TollPlazaRepositoryTest {

  @Autowired private TollPlazaRepository tollPlazaRepository;

  @Autowired private TestEntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManager.persist(new TollPlaza("Plaza 1", 28.5, 77.1, "Delhi"));
    entityManager.persist(new TollPlaza("Plaza 2", 27.2, 79.3, "UP"));
    entityManager.flush();
  }

  @Test
  void testFindTollPlazasInSegment_ShouldReturnPlazas() {
    List<TollPlaza> results = tollPlazaRepository.findTollPlazasInSegment(27.0, 29.0, 76.0, 78.0);
    assertNotNull(results);
    assertFalse(results.isEmpty());
  }

  @Test
  void testFindTollPlazasInSegment_NoPlazas() {
    List<TollPlaza> results = tollPlazaRepository.findTollPlazasInSegment(35.0, 36.0, 80.0, 81.0);
    assertNotNull(results);
    assertTrue(results.isEmpty());
  }
}
