package domain.objectives;

import domain.Solution;
import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import lombok.var;

public class NumberOfVisitedCountriesObjective implements Objective, VisitNewSiteObjective {

  private final ObjectiveSense sense;

  public NumberOfVisitedCountriesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
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

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution,
      final Site site) {
    final var value = site.getCountries().stream()
        .filter(country -> !solution.isVisitingCountry(country))
        .count();
    return ObjectiveValue.builder()
        .sense(sense)
        .value(value)
        .build();
  }
}
