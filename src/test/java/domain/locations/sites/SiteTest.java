package domain.locations.sites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.locations.Coordinates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SiteTest {

  private String name;
  private int siteNumber;
  private Coordinates coordinates;
  private Country country1;
  private List<Country> countries;
  private SiteType siteType;
  private Site site;

  @BeforeEach
  public void setUp() {
    name = "Home";
    siteNumber = 24;
    coordinates = new Coordinates(2.45, 1.903);
    country1 = new Country("France");
    final Country country2 = new Country("England");
    siteType = SiteType.Natural;
    countries = new ArrayList<>(Arrays.asList(country1, country2));
    site =
        Site.builder()
            .name(name)
            .locationID(siteNumber)
            .coordinates(coordinates)
            .country(country1)
            .country(country2)
            .type(siteType)
            .isEndangered()
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
  public void is_natural_test() {
    assertEquals(siteType.isNatural(), site.isNatural());
  }

  @Test
  public void is_cultural_test() {
    assertEquals(siteType.isCultural(), site.isCultural());
  }

  @Test
  public void is_endangered_test() {
    assertTrue(site.isEndangered());
  }

  @Test
  public void equals_returns_true() {
    final var other =
        Site.builder()
            .name(name)
            .locationID(siteNumber)
            .coordinates(coordinates)
            .countries(countries)
            .type(siteType)
            .isEndangered()
            .build();
    assertEquals(site, other);
  }

  @Test
  public void equals_returns_false() {
    final var other =
        Site.builder()
            .name(name)
            .locationID(siteNumber)
            .coordinates(coordinates)
            .country(country1) // missing country2
            .type(siteType)
            .isEndangered()
            .build();
    assertNotEquals(site, other);
  }

  @Test
  public void test_to_string_for_not_endangered_site() {
    final var country1 = new Country("Country1");
    final var country2 = new Country("Country2");
    final var country3 = new Country("Country3");
    final var site =
        Site.builder()
            .name("Super cool name")
            .locationID(1)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .country(country1)
            .country(country2)
            .country(country3)
            .type(SiteType.Cultural)
            .build();
    final var expectedResult = "Super cool name / Country1, Country2 and Country3 / Cultural";
    assertEquals(expectedResult, site.toString());
  }

  @Test
  public void test_to_string_for_endangered_site() {
    final var country1 = new Country("Country1");
    final var country2 = new Country("Country2");
    final var country3 = new Country("Country3");
    final var site =
        Site.builder()
            .name("Super cool name")
            .locationID(1)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .country(country1)
            .country(country2)
            .country(country3)
            .type(SiteType.Cultural)
            .isEndangered()
            .build();
    final var expectedResult =
        "Super cool name / Country1, Country2 and Country3 / Cultural / Endangered";
    assertEquals(expectedResult, site.toString());
  }
}
