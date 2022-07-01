package domain.constraints;

import domain.Solution;
import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.matrix.TravelMatrix;
import domain.matrix.computers.HaversineComputer;
import domain.matrix.computers.TravelTimeComputer;
import domain.site.Site;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.var;

public class MaxTripDurationConstraint implements Constraint, VisitNewSiteConstraint {

  private static final long maxDuration = Duration.of(3, ChronoUnit.WEEKS).getSeconds();

  @Override
  public boolean isFeasible(final Solution solution) {
    return solution.getTripDuration() <= maxDuration;
  }

  @Override
  public boolean canVisitNewSite(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix) {
    return solution.getTripDuration()
        + Solution.timePerSite
        + getTimeFromPrevious(solution, site, position, matrix)
        + getTimeToNext(solution, site, position, matrix)
        - getRemovedTime(solution, position, matrix) <= maxDuration;
  }

  private long getTimeFromPrevious(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix) {
    if (position == 0) {
      return TravelTimeComputer.convertToTime(
          HaversineComputer.getDistance(solution.getStart(), site.getCoordinates()));
    }
    return matrix.time(solution.getVisitedSites().get(position-1), site);
  }

  private long getTimeToNext(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix) {
    if (position == solution.getVisitedSites().size()) {
      return TravelTimeComputer.convertToTime(
          HaversineComputer.getDistance(site.getCoordinates(), solution.getStart()));
    }
    return matrix.time(site, solution.getVisitedSites().get(position));
  }

  /**
   * @return seconds to travel from place that would be before position to place that would be after
   */
  private long getRemovedTime(final Solution solution, final int position, final TravelMatrix matrix) {
    final var visitedSites = solution.getVisitedSites();
    if (visitedSites.isEmpty()) {
      return 0L;
    }
    if (position == 0) {
      return TravelTimeComputer.convertToTime(
          HaversineComputer.getDistance(solution.getStart(), visitedSites.get(0).getCoordinates()));
    }
    if (position == visitedSites.size()) {
      final var lastVisitedSite = visitedSites.get(visitedSites.size()-1);
      return TravelTimeComputer.convertToTime(
          HaversineComputer.getDistance(lastVisitedSite.getCoordinates(), solution.getStart()));
    }
    return matrix.time(visitedSites.get(position-1), visitedSites.get(position));
  }
}
