package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CityTest {

  private String name;
  private Coordinates coordinates;
  private City city;

  @BeforeEach
  public void setUp() {
    name = "London";
    coordinates = new Coordinates(-3.45, 4.56);
    city = new City(name, coordinates);
  }

  @Test
  public void get_name_test() {
    assertEquals(name, city.getName());
  }

  @Test
  public void get_coordinates_test() {
    assertEquals(coordinates, city.getCoordinates());
  }

  @Test
  public void equals_returns_true() {
    final var sameCoord = new Coordinates(coordinates.getLatitude(), coordinates.getLongitude());
    final var other = new City(name, sameCoord);
    assertEquals(city, other);
  }

  @Test
  public void equals_returns_false_because_of_name() {
    final var sameCoord = new Coordinates(coordinates.getLatitude(), coordinates.getLongitude());
    final var other = new City("Manchester", sameCoord);
    assertNotEquals(city, other);
  }

  @Test
  public void equals_returns_false_because_of_coordinates() {
    final var diffCoord = new Coordinates(0d, 0d);
    final var other = new City(name, diffCoord);
    assertNotEquals(city, other);
  }

}
