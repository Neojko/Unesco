package domain.objectives;

import domain.Solution;

public class NumberOfVisitedCountriesObjective implements Objective {

  private final ObjectiveSense sense;

  public NumberOfVisitedCountriesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveName getObjectiveName() {
    return ObjectiveName.NUMBER_OF_VISITED_COUNTRIES;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    return ObjectiveValue.builder()
        .sense(sense)
        .value(solution.getVisitedCountries().size())
        .build();
  }
}
