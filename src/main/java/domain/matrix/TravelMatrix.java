package domain.matrix;

import domain.matrix.computers.HaversineComputer;
import domain.matrix.computers.TravelTimeComputer;
import domain.site.Site;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.var;

public class TravelMatrix {

  // Map Site unique number -> index in below array
  private final Map<Integer, Integer> indices;
  private final double[][] travelTimeInSeconds;

  public TravelMatrix(final List<Site> sites) {
    final var uniqueNumbers =
        sites.stream().map(Site::getUniqueNumber).collect(Collectors.toList());
    indices =
        IntStream.range(0, sites.size())
            .boxed()
            .collect(Collectors.toMap(uniqueNumbers::get, i -> i));
    travelTimeInSeconds = new double[sites.size()][sites.size()];
    for (int i = 0; i < sites.size(); i++) {
      final var firstSiteIndex = indices.get(sites.get(i).getUniqueNumber());
      travelTimeInSeconds[firstSiteIndex][firstSiteIndex] = 0d;
      for (int j = i + 1; j < sites.size(); j++) {
        final var secondSiteIndex = indices.get(sites.get(j).getUniqueNumber());
        final var distance =
            HaversineComputer.getDistance(
                sites.get(i).getCoordinates(), sites.get(j).getCoordinates());
        final var time = TravelTimeComputer.convertToTime(distance);
        travelTimeInSeconds[firstSiteIndex][secondSiteIndex] = time;
        travelTimeInSeconds[secondSiteIndex][firstSiteIndex] = time;
      }
    }
  }

  /** @return travel time in seconds between first and second Site */
  public double time(final Site first, final Site second) {
    final var firstIndex = indices.get(first.getUniqueNumber());
    final var secondIndex = indices.get(second.getUniqueNumber());
    return travelTimeInSeconds[firstIndex][secondIndex];
  }
}
