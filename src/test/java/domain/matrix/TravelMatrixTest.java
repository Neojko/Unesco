package domain.matrix;

import domain.site.Coordinates;
import domain.site.Country;
import domain.site.Site;
import domain.site.SiteStatus;
import java.util.Arrays;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TravelMatrixTest {

  private Coordinates coordinates1, coordinates2;
  private Site site1, site2;
  private TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    coordinates1 = new Coordinates(0.345918, -34.109321);
    coordinates2 = new Coordinates(-89.342565, 43.652214);
    final var country = new Country("England");
    final var status = SiteStatus.builder().build();
    site1 = new Site("site1", coordinates1, country, status);
    site2 = new Site("site2", coordinates2, country, status);
    matrix = new TravelMatrix(Arrays.asList(coordinates1, coordinates2));
  }

  @Test
  public void test_time_from_site_to_itself() {
    Assertions.assertEquals(0d, matrix.time(site1, site1));
    Assertions.assertEquals(0d, matrix.time(site2, site2));
  }

  @Test
  public void test_time_from_two_six_digits_coordinates() {
    final var distance = HaversineComputer.getDistance(coordinates1, coordinates2);
    Assertions.assertEquals(distance * 3600 / 80, matrix.time(site1, site2));
  }
}
