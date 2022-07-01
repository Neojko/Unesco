package domain.matrix.computers;

import domain.locations.Coordinates;
import lombok.var;

public class TravelTimeComputer {

  // Travel speed in kilometers per hour
  private static final double travelSpeed = 80;

  /** @return travel time to cover distance between two coordinates in seconds */
  public static long convertToTime(final Coordinates first, final Coordinates second) {
    final var distance = HaversineComputer.getDistance(first, second);
    return convertToTime(distance);
  }

  /**
   * @param distance: distance in kilometers
   * @return travel time to cover distance in seconds
   */
  public static long convertToTime(final double distance) {
    return (long) (distance * 3600 / travelSpeed);
  }
}
