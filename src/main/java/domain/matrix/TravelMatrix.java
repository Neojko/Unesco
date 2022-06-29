package domain.matrix;

import domain.matrix.computers.HaversineComputer;
import domain.matrix.computers.TravelTimeComputer;
import domain.site.Site;
import domain.site.SiteNumber;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.var;

public class TravelMatrix {

  private final Map<SiteNumber, Integer> indices;
  private final long[][] travelTimeInSeconds;

  public TravelMatrix(final List<Site> sites) {
    indices = initIndices(sites);
    travelTimeInSeconds = new long[sites.size()][sites.size()];
    for (int i = 0; i < sites.size(); i++) {
      final var firstSiteIndex = indices.get(sites.get(i).getNumber());
      travelTimeInSeconds[firstSiteIndex][firstSiteIndex] = 0;
      for (int j = i + 1; j < sites.size(); j++) {
        final var secondSiteIndex = indices.get(sites.get(j).getNumber());
        final var distance =
            HaversineComputer.getDistance(
                sites.get(i).getCoordinates(), sites.get(j).getCoordinates());
        final var time = TravelTimeComputer.convertToTime(distance);
        travelTimeInSeconds[firstSiteIndex][secondSiteIndex] = time;
        travelTimeInSeconds[secondSiteIndex][firstSiteIndex] = time;
      }
    }
  }

  public TravelMatrix(final String inputCSVFileName) {
    // Build iterator
    // While size of iterator.next().split(",") > 3 => create sites
    // Until end of file, add matrix values (i j and j i)
  }

  public void exportToCSV() {
    // Convert all sites to short format
    // For all i and j add lines number,number,time (do i j for i j and j i)
  }

  /** @return travel time in seconds between first and second Site */
  public double time(final Site first, final Site second) {
    final var firstIndex = indices.get(first.getNumber());
    final var secondIndex = indices.get(second.getNumber());
    return travelTimeInSeconds[firstIndex][secondIndex];
  }

  private Map<SiteNumber, Integer> initIndices(final List<Site> sites) {
    final var uniqueNumbers = sites.stream().map(Site::getNumber).collect(Collectors.toList());
    return IntStream.range(0, sites.size())
            .boxed()
            .collect(Collectors.toMap(uniqueNumbers::get, i -> i));
  }
}
