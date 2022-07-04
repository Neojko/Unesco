package domain.solution;

import domain.locations.Location;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.var;

public class SolutionTripDurationComputer {

  public static final long timePerSite = Duration.of(6, ChronoUnit.HOURS).getSeconds();

  public static long computeTripDuration(final Solution solution, final TravelMatrix matrix) {
    final var visitedSites = solution.getVisitedSites().getSites();
    if (visitedSites.isEmpty()) {
      return 0L;
    }
    long result = matrix.time(solution.getStart(), visitedSites.get(0));
    for (int i = 0; i < visitedSites.size() - 1; i++) {
      result += matrix.time(visitedSites.get(i), visitedSites.get(i + 1));
    }
    final var lastVisitedSite = visitedSites.get(visitedSites.size() - 1);
    result += matrix.time(lastVisitedSite, solution.getStart());
    result += visitedSites.size() * timePerSite;
    return result;
  }

  public static long computeTripDurationDeltaToVisitNewSite(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix) {
    final var previous = getPreviousLocation(solution, position);
    final var next = getNextLocationIfInserting(solution, position);
    return matrix.time(previous, site)
        + timePerSite
        + matrix.time(site, next)
        - matrix.time(previous, next);
  }

  public static long computeTripDurationDeltaToUnvisitSite(
      final Solution solution, final Site site, final TravelMatrix matrix) {
    final var position = solution.getVisitedSites().getSites().indexOf(site);
    final var previous = getPreviousLocation(solution, position);
    final var next = getNextLocation(solution, position);
    return matrix.time(previous, next)
        - matrix.time(previous, site)
        - timePerSite
        - matrix.time(site, next);
  }

  private static Location getPreviousLocation(final Solution solution, final int position) {
    if (position == 0) {
      return solution.getStart();
    }
    return solution.getVisitedSites().getSites().get(position - 1);
  }

  private static Location getNextLocation(final Solution solution, final int position) {
    if (position == solution.getVisitedSites().getSites().size()-1) {
      return solution.getStart();
    }
    return solution.getVisitedSites().getSites().get(position+1);
  }

  private static Location getNextLocationIfInserting(final Solution solution, final int position) {
    if (position == solution.getVisitedSites().getSites().size()) {
      return solution.getStart();
    }
    return solution.getVisitedSites().getSites().get(position);
  }
}
