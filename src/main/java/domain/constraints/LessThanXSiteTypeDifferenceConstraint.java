package domain.constraints;

import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.UnvisitSiteConstraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.locations.sites.Site;
import domain.solution.Solution;
import lombok.Builder;
import lombok.var;

/**
 * This constraints checks that the absolute difference between number of cultural and natural
 * visited sites is no more than an allowed one.
 */

@Builder
public class LessThanXSiteTypeDifferenceConstraint
    implements Constraint, VisitNewSiteConstraint, UnvisitSiteConstraint {

  private final int maxDifference;

  @Override
  public boolean isFeasible(final Solution solution) {
    return isConstraintFeasible(solution, 0, 0);
  }

  @Override
  public boolean canVisitNewSite(final Solution solution, final Site site, final int position,
      final long tripDurationIncrease) {
    return isConstraintFeasible(solution, site.isCultural() ? 1 : 0, site.isCultural() ? 1 : 0);
  }

  @Override
  public boolean canUnvisitSite(final Solution solution, final Site site) {
    return isConstraintFeasible(solution, site.isCultural() ? -1 : 0, site.isCultural() ? -1 : 0);
  }

  /**
   * @param solution: solution that would be impacted
   * @param culturalDelta: number of cultural sites to add (if positive) / remove (if negative)
   * @param naturalDelta: number of natural sites to add (if positive) / remove (if negative)
   * @return true if new difference <= maxDifference, false otherwise
   */
  private boolean isConstraintFeasible(
      final Solution solution,
      final int culturalDelta,
      final int naturalDelta) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites() + culturalDelta;
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites() + naturalDelta;
    return Math.abs(cultural - natural) <= maxDifference;
  }
}
