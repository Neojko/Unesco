package domain.matrix.computers;

import domain.locations.Coordinates;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TravelTimeComputerTest {

  @Test
  public void compute_travel_time_test() {
    Assertions.assertEquals(1190L, TravelTimeComputer.convertToTime(26.45));
  }

  @Test
  public void compute_travel_time_between_two_coordinates() {
    final var first = new Coordinates(0.345918, -34.109321);
    final var second = new Coordinates(-89.345692, 138.342918);
    final var expectedDistance = HaversineComputer.getDistance(first, second);
    final var expectedTime = TravelTimeComputer.convertToTime(expectedDistance);
    Assertions.assertEquals(expectedTime, TravelTimeComputer.convertToTime(first, second));
  }
}
