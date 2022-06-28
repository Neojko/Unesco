package domain;

import domain.site.Coordinates;
import domain.site.Site;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.var;

public class TravelMatrix {

  // Travel speed in meters per hour
  private static final double travelSpeed = 80000;

  private final Map<Site, Integer> indices;
  private final double[][] travelTimeInSeconds;

  public TravelMatrix(List<Site> sites) {
    indices = IntStream.range(0, sites.size())
        .boxed()
        .collect(Collectors.toMap(sites::get, i -> i));
    travelTimeInSeconds = new double[sites.size()][sites.size()];
    for (int i = 0; i < sites.size(); i++) {
      final Site first = sites.get(i);
      travelTimeInSeconds[indices.get(first)][indices.get(first)] = 0d;
      for (int j = i + 1; j < sites.size(); j++) {
        final Site second = sites.get(j);
        final var distance = getHaversine(first.getCoordinates(), second.getCoordinates());
        final var time = distance / travelSpeed;
        travelTimeInSeconds[indices.get(first)][indices.get(second)] = time;
        travelTimeInSeconds[indices.get(second)][indices.get(first)] = time;
      }
    }
  }

  /** @return travel time in seconds between first and second Site */
  public double time(final Site first, final Site second) {
    return travelTimeInSeconds[indices.get(first)][indices.get(second)];
  }

  /** @return Haversine distance in meters between two Coordinates */
  private double getHaversine(final Coordinates first, final Coordinates second) {
    final var latDistance = toRadius(second.getLatitude() - first.getLatitude());
    final var lonDistance = toRadius(second.getLongitude() - first.getLongitude());
    final var a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(toRadius(first.getLatitude()))
                * Math.cos(toRadius(second.getLatitude()))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
    final var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return 6371d * c;
  }

  private double toRadius(final double value) {
    return value * Math.PI / 180;
  }
}
