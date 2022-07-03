package domain.objectives;

import domain.Solution;
import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import lombok.var;

/**
 * This Objective aims to minimise the absolute difference between natural and cultural sites It
 * exists as an Objective instead of as a Constraint in order to explore solutions that would not be
 * feasible if it was a Constraint.
 */
public class SiteTypeParityObjective implements Objective, VisitNewSiteObjective {

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
    final var cultural = solution.getNumberOfCulturalVisitedSites();
    final var natural = solution.getNumberOfNaturalVisitedSites();
    final var value = Math.abs(cultural - natural);
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site) {
    final var cultural = solution.getNumberOfCulturalVisitedSites() + (site.isCultural() ? 1 : 0);
    final var natural = solution.getNumberOfNaturalVisitedSites() + (site.isNatural() ? 1 : 0);
    final var value = Math.abs(cultural - natural);
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }
}
