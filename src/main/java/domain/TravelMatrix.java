package domain;

import domain.site.City;
import domain.site.Coordinates;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.var;

/**
 * @author XXX
 */
public class TravelMatrix {

  // Travel speed in meters per hour
  private final static double travelSpeed = 80000;

  private final Map<City, Integer> indices;
  private final double[][] travelTimeInSeconds;

  public TravelMatrix(List<City> cities) {
    indices = IntStream.range(0, cities.size())
        .boxed()
        .collect(Collectors.toMap(cities::get, i -> i));
    travelTimeInSeconds = new double[cities.size()][cities.size()];
    for (int i = 0; i < cities.size(); i++) {
      final City first = cities.get(i);
      travelTimeInSeconds[indices.get(first)][indices.get(first)] = 0d;
      for (int j = i+1; j < cities.size(); j++) {
        final City second = cities.get(j);
        final var distance = getHaversine(first.getCoordinates(), second.getCoordinates());
        final var time = distance / travelSpeed;
        travelTimeInSeconds[indices.get(first)][indices.get(second)] = time;
        travelTimeInSeconds[indices.get(second)][indices.get(first)] = time;
      }
    }
  }

  /**
   * @return travel time in seconds between first and second City
   */
  public double time(final City first, final City second) {
    return travelTimeInSeconds[indices.get(first)][indices.get(second)];
  }

  /**
   * @return Haversine distance in meters between two Coordinates
   */
  private double getHaversine(final Coordinates first, final Coordinates second) {
    final var latDistance = toRadius(second.getLatitude() - first.getLatitude());
    final var lonDistance = toRadius(second.getLongitude() - first.getLongitude());
    final var a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(toRadius(first.getLatitude())) * Math.cos(toRadius(second.getLatitude()))
        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    final var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return 6371d * c;
  }

  private double toRadius(final double value) {
    return value * Math.PI / 180;
  }

}
