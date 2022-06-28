package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoordinatesTest {

  private double latitude;
  private double longitude;
  private Coordinates coordinates;

  @BeforeEach
  public void setUp() {
    latitude = 3.455;
    longitude = -4.356;
    coordinates = new Coordinates(latitude, longitude);
  }

  @Test
  public void get_latitude_test() {
    assertEquals(latitude, coordinates.getLatitude());
  }

  @Test
  public void get_longitude_test() {
    assertEquals(longitude, coordinates.getLongitude());
  }

  @Test
  public void equals_returns_true() {
    final var other = new Coordinates(latitude, longitude);
    assertEquals(coordinates, other);
  }

  @Test
  public void equals_returns_false_because_of_latitude() {
    final var other = new Coordinates(1.34, longitude);
    assertNotEquals(coordinates, other);
  }

  @Test
  public void equals_returns_false_because_of_longitude() {
    final var other = new Coordinates(latitude, 1.34);
    assertNotEquals(coordinates, other);
  }
}
