package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SiteTest {

  private String name;
  private int uniqueNumber;
  private Coordinates coordinates;
  private Country country1;
  private List<Country> countries;
  private SiteStatus status;
  private Site site;

  @BeforeEach
  public void setUp() {
    name = "Eiffel Tower";
    uniqueNumber = 24;
    coordinates = new Coordinates(2.45, 1.903);
    country1 = new Country("France");
    final Country country2 = new Country("England");
    countries = new ArrayList<>(Arrays.asList(country1, country2));
    status = SiteStatus.builder().isCultural(true).build();
    site =
        Site.builder()
            .name(name)
            .uniqueNumber(uniqueNumber)
            .coordinates(coordinates)
            .country(country1)
            .country(country2)
            .status(status)
            .build();
  }

  @Test
  public void get_name_test() {
    assertEquals(name, site.getName());
  }

  @Test
  public void get_coordinates_test() {
    assertEquals(coordinates, site.getCoordinates());
  }

  @Test
  public void get_countries_test() {
    assertEquals(countries, site.getCountries());
  }

  @Test
  public void get_status_test() {
    assertEquals(status, site.getStatus());
  }

  @Test
  public void is_natural_test() {
    assertEquals(status.isNatural(), site.isNatural());
  }

  @Test
  public void is_cultural_test() {
    assertEquals(status.isCultural(), site.isCultural());
  }

  @Test
  public void is_endangered_test() {
    assertEquals(status.isEndangered(), site.isEndangered());
  }

  @Test
  public void equals_returns_true() {
    final var other =
        Site.builder()
            .name(name)
            .uniqueNumber(uniqueNumber)
            .coordinates(coordinates)
            .countries(countries)
            .status(status)
            .build();
    assertEquals(site, other);
  }

  @Test
  public void equals_returns_false() {
    final var other =
        Site.builder()
            .name(name)
            .uniqueNumber(uniqueNumber)
            .coordinates(coordinates)
            .country(country1) // missing country2
            .status(status)
            .build();
    assertNotEquals(site, other);
  }
}
