package domain.objectives;

import domain.Solution;

public class NumberOfVisitedSitesObjective implements Objective {

  private final ObjectiveSense sense;

  public NumberOfVisitedSitesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveName getObjectiveName() {
    return ObjectiveName.NUMBER_OF_VISITED_SITES;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    return ObjectiveValue.builder()
        .sense(sense)
        .value(solution.getVisitedSites().size())
        .build();
  }
}
