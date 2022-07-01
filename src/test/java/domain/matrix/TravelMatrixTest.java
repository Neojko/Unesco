package domain.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.locations.Coordinates;
import domain.locations.Location;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.locations.sites.SiteReader;
import domain.locations.sites.SiteReaderTest;
import domain.matrix.computers.HaversineComputer;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TravelMatrixTest {

  private Coordinates coordinates1, coordinates2;
  private Site site1, site2;
  private List<Location> locations;
  private TravelMatrix matrix;
  private String outputFileName;

  @BeforeEach
  public void setUp() {
    coordinates1 = new Coordinates(0.345918, -34.109321);
    coordinates2 = new Coordinates(-89.342565, 43.652214);
    site1 = Site.builder().coordinates(coordinates1).locationID(1).build();
    site2 = Site.builder().coordinates(coordinates2).locationID(2).build();
    locations = Arrays.asList(site1, site2);
    matrix = new TravelMatrix(locations);
    outputFileName = "src/test/resources/export.csv";
  }

  @Test
  public void test_time_from_site_to_itself() {
    assertEquals(0d, matrix.time(site1, site1));
    assertEquals(0d, matrix.time(site2, site2));
  }

  @Test
  public void test_matrix_equals_expected_values() {
    for (int i = 0; i < locations.size(); i++) {
      for (final Location location : locations) {
        final var firstLocation = locations.get(i);
        final var firstCoordinates = firstLocation.getCoordinates();
        final var secondCoordinates = location.getCoordinates();
        final var distance = HaversineComputer.getDistance(firstCoordinates, secondCoordinates);
        assertEquals((long) (distance * 3600 / 80), matrix.time(firstLocation, location));
      }
    }
  }

  @Test
  public void matrix_stays_same_when_exported_and_read_from_export() throws IOException {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var start = TravelStartLocation.builder().coordinates(new Coordinates(0, 0)).build();
    final var matrix = new TravelMatrix(sites, start);
    matrix.exportToCSV(outputFileName);
    final var newMatrix = new TravelMatrix(sites, outputFileName, start);
    assertEquals(matrix, newMatrix);
  }

  @AfterEach
  public void deleteFiles() {
    final File outputFile = new File(outputFileName);
    outputFile.deleteOnExit();
  }
}
