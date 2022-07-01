package domain.objectives;

import domain.Solution;
import domain.objectives.components.Objective;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;

public class NumberOfVisitedSitesObjective implements Objective {

  private final ObjectiveSense sense;

  public NumberOfVisitedSitesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    return ObjectiveValue.builder().sense(sense).value(solution.getVisitedSites().size()).build();
  }
}
