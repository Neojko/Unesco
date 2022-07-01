package domain.objectives;

import domain.Solution;
import domain.objectives.components.ObjectiveName;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;

public class NumberOfVisitedCountriesObjective implements Objective {

  private final ObjectiveSense sense;

  public NumberOfVisitedCountriesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveName getName() {
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
