package domain.constraints;

import domain.Solution;
import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.locations.Location;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import lombok.AllArgsConstructor;
import lombok.var;

@AllArgsConstructor
public class MaxTripDurationConstraint implements Constraint, VisitNewSiteConstraint {

  private final long maxDurationInSeconds;

  @Override
  public boolean isFeasible(final Solution solution) {
    return solution.getDurationInSeconds() <= maxDurationInSeconds;
  }

  @Override
  public boolean canVisitNewSite(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix) {
    final var previous = getPreviousLocation(solution, position);
    final var next = getNextLocation(solution, position);
    return solution.getDurationInSeconds()
            + Solution.timePerSite
            + matrix.time(previous, site)
            + matrix.time(site, next)
            - matrix.time(previous, next)
        <= maxDurationInSeconds;
  }

  private Location getPreviousLocation(final Solution solution, final int position) {
    if (position == 0) {
      return solution.getStart();
    }
    return solution.getVisitedSites().get(position - 1);
  }

  private Location getNextLocation(final Solution solution, final int position) {
    if (position == solution.getVisitedSites().size()) {
      return solution.getStart();
    }
    return solution.getVisitedSites().get(position);
  }
}
