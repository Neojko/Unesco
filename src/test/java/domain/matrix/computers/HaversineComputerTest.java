package domain.matrix.computers;

import domain.site.Coordinates;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * All compared values come from https://www.vcalc.com/wiki/vCalc/Haversine+-+Distance The website
 * might not be the best, but the first two links when googling "Get Haversine distance" already
 * don't give the same result.
 */
public class HaversineComputerTest {

  private Coordinates minCoordinates, sixDigitsCoordinates1, sixDigitsCoordinates2, maxCoordinates;

  @BeforeEach
  public void setUp() {
    minCoordinates = new Coordinates(-90d, -180d);
    sixDigitsCoordinates1 = new Coordinates(0.345918, -34.109321);
    sixDigitsCoordinates2 = new Coordinates(-89.345692, 138.342918);
    maxCoordinates = new Coordinates(90d, 180d);
  }

  @Test
  public void test_distance_from_site_to_itself() {
    final var distance = HaversineComputer.getDistance(minCoordinates, minCoordinates);
    Assertions.assertEquals(0d, distance);
  }

  @Test
  public void test_correct_distance_from_min_to_max_site() {
    final var distance = HaversineComputer.getDistance(minCoordinates, maxCoordinates);
    Assertions.assertTrue(isMinimeError(20015.12, distance));
  }

  @Test
  public void test_correct_distance_with_min_site() {
    final var distance = HaversineComputer.getDistance(minCoordinates, sixDigitsCoordinates1);
    Assertions.assertTrue(isMinimeError(10046.02, distance));
  }

  @Test
  public void test_correct_distance_with_max_site() {
    final var distance = HaversineComputer.getDistance(sixDigitsCoordinates1, maxCoordinates);
    Assertions.assertTrue(isMinimeError(9969.09, distance));
  }

  @Test
  public void test_correct_distance_with_two_six_digits_sites() {
    final var distance =
        HaversineComputer.getDistance(sixDigitsCoordinates1, sixDigitsCoordinates2);
    Assertions.assertTrue(isMinimeError(10118.15, distance));
  }

  private boolean isMinimeError(final double expected, final double actual) {
    return Math.abs(expected - actual) < 0.01;
  }
}
