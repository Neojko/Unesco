package domain.objectives;

import domain.Solution;
import domain.site.Site;
import lombok.var;

public class NumberOfVisitedEndangeredSitesObjective implements Objective {

  private final ObjectiveSense sense;

  public NumberOfVisitedEndangeredSitesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveName getObjectiveName() {
    return ObjectiveName.NUMBER_OF_ENDANGERED_VISITED_SITES;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    final var value = solution.getVisitedSites().stream().filter(Site::isEndangered).count();
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }
}