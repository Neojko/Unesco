package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CountryTest {

  private String name;
  private Country country;

  @BeforeEach
  public void setUp() {
    name = "France";
    country = new Country(name);
  }

  @Test
  public void get_name_test() {
    assertEquals(name, country.getName());
  }

  @Test
  public void equals_returns_true() {
    final var other = new Country("France");
    assertEquals(country, other);
  }

  @Test
  public void equals_returns_false() {
    final var other = new Country("England");
    assertNotEquals(country, other);
  }
}
