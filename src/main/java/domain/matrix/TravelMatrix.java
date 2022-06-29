package domain.matrix;

import domain.site.Site;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.var;

public class TravelMatrix {

  // Travel speed in kilometers per hour
  private static final double travelSpeed = 80;

  private final Map<Site, Integer> indices;
  private final double[][] travelTimeInSeconds;

  public TravelMatrix(List<Site> sites) {
    indices =
        IntStream.range(0, sites.size()).boxed().collect(Collectors.toMap(sites::get, i -> i));
    travelTimeInSeconds = new double[sites.size()][sites.size()];
    for (int i = 0; i < sites.size(); i++) {
      final Site first = sites.get(i);
      travelTimeInSeconds[indices.get(first)][indices.get(first)] = 0d;
      for (int j = i + 1; j < sites.size(); j++) {
        final Site second = sites.get(j);
        final var distance =
            HaversineComputer.getDistance(first.getCoordinates(), second.getCoordinates());
        final var time = distance * 3600 / travelSpeed;
        travelTimeInSeconds[indices.get(first)][indices.get(second)] = time;
        travelTimeInSeconds[indices.get(second)][indices.get(first)] = time;
      }
    }
  }

  /** @return travel time in seconds between first and second Site */
  public double seconds(final Site first, final Site second) {
    return travelTimeInSeconds[indices.get(first)][indices.get(second)];
  }
}
