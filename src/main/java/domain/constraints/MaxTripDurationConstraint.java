package domain.constraints;

import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.locations.sites.Site;
import domain.solution.Solution;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MaxTripDurationConstraint implements Constraint, VisitNewSiteConstraint {

  private final long maxTripDurationInSeconds;

  @Override
  public boolean isFeasible(final Solution solution) {
    return solution.getTripDurationinSeconds() <= maxTripDurationInSeconds;
  }

  @Override
  public boolean canVisitNewSite(
      final Solution solution,
      final Site site,
      final int position,
      final long tripDurationIncrease) {
    return solution.getTripDurationinSeconds() + tripDurationIncrease <= maxTripDurationInSeconds;
  }
}
