package domain.matrix;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.matrix.computers.HaversineComputer;
import domain.site.Coordinates;
import domain.site.Site;
import domain.site.SiteReader;
import domain.site.SiteReaderTest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TravelMatrixTest {

  private Coordinates coordinates1, coordinates2;
  private Site site1, site2;
  private TravelMatrix matrix;
  private String outputFileName;

  @BeforeEach
  public void setUp() {
    coordinates1 = new Coordinates(0.345918, -34.109321);
    coordinates2 = new Coordinates(-89.342565, 43.652214);
    site1 = Site.builder().coordinates(coordinates1).uniqueNumber(1).build();
    site2 = Site.builder().coordinates(coordinates2).uniqueNumber(2).build();
    matrix = new TravelMatrix(Arrays.asList(site1, site2));
    outputFileName = "src/test/resources/export.csv";
  }

  @Test
  public void test_time_from_site_to_itself() {
    assertEquals(0d, matrix.time(site1, site1));
    assertEquals(0d, matrix.time(site2, site2));
  }

  @Test
  public void test_time_from_two_six_digits_coordinates() {
    final var distance = HaversineComputer.getDistance(coordinates1, coordinates2);
    assertEquals((long) (distance * 3600 / 80), matrix.time(site1, site2));
  }

  @Test
  public void test_equals() throws IOException {
    matrix.exportToCSV(outputFileName);
    final var sites = new ArrayList<>(Arrays.asList(site1, site2));
    final var newMatrix = new TravelMatrix(sites, outputFileName);
    assertEquals(matrix, newMatrix);
    assertEquals(matrix.time(site1, site1), newMatrix.time(site1, site1));
    assertEquals(matrix.time(site1, site2), newMatrix.time(site1, site2));
    assertEquals(matrix.time(site2, site1), newMatrix.time(site2, site1));
    assertEquals(matrix.time(site2, site2), newMatrix.time(site2, site2));
  }

  @Test
  public void matrix_stays_same_when_exported_and_read_from_export() throws IOException {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var matrix = new TravelMatrix(sites);
    matrix.exportToCSV(outputFileName);
    final var newMatrix = new TravelMatrix(sites, outputFileName);
    assertEquals(matrix, newMatrix);
  }

  @AfterEach
  public void deleteFiles() {
    final File outputFile = new File(outputFileName);
    outputFile.deleteOnExit();
  }
}
