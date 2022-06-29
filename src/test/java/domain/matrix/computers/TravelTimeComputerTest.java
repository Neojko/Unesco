package domain.matrix.computers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TravelTimeComputerTest {

  @Test
  public void compute_travel_time_test() {
    Assertions.assertEquals(1190L, TravelTimeComputer.convertToTime(26.45));
  }
}
