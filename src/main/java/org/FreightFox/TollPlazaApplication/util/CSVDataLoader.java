package org.FreightFox.TollPlazaApplication.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.FreightFox.TollPlazaApplication.model.TollPlaza;
import org.FreightFox.TollPlazaApplication.repository.TollPlazaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSVDataLoader {

  private final TollPlazaRepository tollPlazaRepository;
  private static final String CSV_FILE_PATH = "src/main/resources/toll_plaza_india.csv";

  @PostConstruct
  public void loadTollPlazas() {
    if (tollPlazaRepository.count() > 0) {
      System.out.println("Toll plaza data already loaded.");
      return;
    }

    try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
      List<String[]> records = reader.readAll();
      records.remove(0); // Remove header row

      for (String[] record : records) {
        TollPlaza plaza = new TollPlaza();
        plaza.setLongitude(Double.parseDouble(record[0]));
        plaza.setLatitude(Double.parseDouble(record[1]));
        plaza.setName(record[2]);
        plaza.setGeoState(record[3]);

        tollPlazaRepository.save(plaza);
      }

      System.out.println("CSV Data Loaded Successfully!");
    } catch (IOException | CsvException e) {
      e.printStackTrace();
    }
  }
}
