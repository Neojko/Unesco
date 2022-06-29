package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SiteTest {

  private String name;
  private Coordinates coordinates;
  private Country country1;
  private List<Country> countries;
  private SiteStatus status;
  private Site site;

  @BeforeEach
  public void setUp() {
    name = "Eiffel Tower";
    coordinates = new Coordinates(2.45, 1.903);
    country1 = new Country("France");
    Country country2 = new Country("England");
    countries = new ArrayList<>(Arrays.asList(country1, country2));
    status = SiteStatus.builder().isCultural(true).build();
    site = new Site(name, coordinates, countries, status);
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
    final var other = new Site(name, coordinates, countries, status);
    assertEquals(site, other);
  }

  @Test
  public void equals_returns_false() {
    final var otherCountries = new ArrayList<>(Collections.singletonList(country1));
    final var other = new Site("Other name", coordinates, otherCountries, status);
    assertNotEquals(site, other);
  }
}
