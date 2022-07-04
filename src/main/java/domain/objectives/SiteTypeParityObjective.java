package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.UnvisitSiteObjective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.var;

/**
 * This Objective aims to minimise the absolute difference between natural and cultural sites It
 * exists as an Objective instead of as a Constraint in order to explore solutions that would not be
 * feasible if it was a Constraint.
 */
public class SiteTypeParityObjective
    implements Objective, VisitNewSiteObjective, UnvisitSiteObjective {

  private final ObjectiveSense sense;

  public SiteTypeParityObjective() {
    this.sense = ObjectiveSense.MINIMIZE;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    final var value = getAbsoluteDifferenceBetweenCulturalAndNaturalSites(solution);
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationIncrease) {
    final var oldValue = getAbsoluteDifferenceBetweenCulturalAndNaturalSites(solution);
    final var visitedSites = solution.getVisitedSites();
    final var cultural = visitedSites.getNumberOfCulturalSites() + (site.isCultural() ? 1 : 0);
    final var natural = visitedSites.getNumberOfNaturalSites() + (site.isNatural() ? 1 : 0);
    final var newValue = Math.abs(cultural - natural);
    return ObjectiveValue.builder().sense(sense).value(newValue - oldValue).build();
  }

  @Override
  public ObjectiveValue getUnvisitSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationIncrease) {
    final var oldValue = getAbsoluteDifferenceBetweenCulturalAndNaturalSites(solution);
    final var visitedSites = solution.getVisitedSites();
    final var cultural = visitedSites.getNumberOfCulturalSites() - (site.isCultural() ? 1 : 0);
    final var natural = visitedSites.getNumberOfNaturalSites() - (site.isNatural() ? 1 : 0);
    final var newValue = Math.abs(cultural - natural);
    return ObjectiveValue.builder().sense(sense).value(newValue - oldValue).build();
  }

  private int getAbsoluteDifferenceBetweenCulturalAndNaturalSites(final Solution solution) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    return Math.abs(cultural - natural);
  }
}
