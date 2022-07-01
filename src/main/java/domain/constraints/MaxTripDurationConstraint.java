package domain.constraints;

import domain.Solution;
import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
    return 0L;
  }

  private long getTimeToNext(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix) {
    return 0L;
  }

  /**
   * @return seconds to travel from place that would be before position to place that would be after
   */
  private long getRemovedTime(final Solution solution, final int position, final TravelMatrix matrix) {
    return 0L;
  }
}
