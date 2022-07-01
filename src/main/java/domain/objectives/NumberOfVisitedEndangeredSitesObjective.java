package domain.objectives;

import domain.Solution;
import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import lombok.var;

public class NumberOfVisitedEndangeredSitesObjective implements Objective {

  private final ObjectiveSense sense;

  public NumberOfVisitedEndangeredSitesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
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
