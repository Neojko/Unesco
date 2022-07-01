package domain.constraints;

import domain.Solution;
import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.matrix.TravelMatrix;
import domain.site.Site;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class MaxTripDurationConstraint implements Constraint, VisitNewSiteConstraint {

  private final static long maxDuration = Duration.of(3, ChronoUnit.WEEKS).getSeconds();

  @Override
  public boolean isFeasible(final Solution solution) {
    return solution.getTripDuration() <= maxDuration;
  }

  @Override
  public boolean canVisitNewSite(
      final Solution solution,
      final Site site,
      final int position,
      final TravelMatrix matrix) {
    return false;
  }
}
