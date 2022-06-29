package domain.matrix;

import domain.site.Coordinates;
import domain.site.Site;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.var;

public class TravelMatrix {

  private final Map<Coordinates, Integer> indices;
  private final double[][] travelTimeInSeconds;

  public TravelMatrix(List<Coordinates> coordinates) {
    indices =
        IntStream.range(0, coordinates.size())
            .boxed()
            .collect(Collectors.toMap(coordinates::get, i -> i));
    travelTimeInSeconds = new double[coordinates.size()][coordinates.size()];
    for (int i = 0; i < coordinates.size(); i++) {
      final Coordinates first = coordinates.get(i);
      travelTimeInSeconds[indices.get(first)][indices.get(first)] = 0d;
      for (int j = i + 1; j < coordinates.size(); j++) {
        final Coordinates second = coordinates.get(j);
        final var distance = HaversineComputer.getDistance(first, second);
        final var time = TravelTimeComputer.convertToTime(distance);
        travelTimeInSeconds[indices.get(first)][indices.get(second)] = time;
        travelTimeInSeconds[indices.get(second)][indices.get(first)] = time;
      }
    }
  }

  /** @return travel time in seconds between first and second Site */
  public double time(final Site first, final Site second) {
    final var firstIndex = indices.get(first.getCoordinates());
    final var secondIndex = indices.get(second.getCoordinates());
    return travelTimeInSeconds[firstIndex][secondIndex];
  }
}
