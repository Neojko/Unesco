package domain.matrix.computers;

public class TravelTimeComputer {

  // Travel speed in kilometers per hour
  private static final double travelSpeed = 80;

  /**
   * @param distance: distance in kilometers
   * @return travel time to cover distance in seconds
   */
  public static double convertToTime(final double distance) {
    return distance * 3600 / travelSpeed;
  }
}
