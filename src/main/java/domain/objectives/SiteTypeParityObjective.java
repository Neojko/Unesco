package domain.objectives;

import domain.Solution;
import domain.objectives.components.ObjectiveName;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import lombok.var;

/**
 * This Objective aims to minimise the absolute difference between natural and cultural sites It
 * exists as an Objective instead of as a Constraint in order to explore solutions that would not be
 * feasible if it was a Constraint.
 */
public class SiteTypeParityObjective implements Objective {

  private final ObjectiveSense sense;

  public SiteTypeParityObjective() {
    this.sense = ObjectiveSense.MINIMIZE;
  }

  @Override
  public ObjectiveName getName() {
    return ObjectiveName.SITE_TYPE_PARITY;
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
}
