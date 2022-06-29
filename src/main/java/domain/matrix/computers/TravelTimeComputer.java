package domain.matrix.computers;

public class TravelTimeComputer {

  // Travel speed in kilometers per hour
  private static final double travelSpeed = 80;

  /**
   * @param distance: distance in kilometers
   * @return travel time to cover distance in seconds
   */
  public static long convertToTime(final double distance) {
    return (long) (distance * 3600 / travelSpeed);
  }
}
