package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SiteTest {

  private String name;
  private Coordinates coordinates;
  private Country country;
  private SiteStatus status;
  private Site site;

  @BeforeEach
  public void setUp() {
    name = "Eiffel Tower";
    coordinates = new Coordinates(2.45, 1.903);
    country = new Country("France");
    status = SiteStatus.builder().isCultural(true).build();
    site = new Site(name, coordinates, country, status);
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
    final var other = new Site(name, coordinates, country, status);
    assertEquals(site, other);
  }

  @Test
  public void equals_returns_false() {
    final var other = new Site("Other name", coordinates, country, status);
    assertNotEquals(site, other);
  }

}
