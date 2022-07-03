package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.var;

public class NumberOfVisitedEndangeredSitesObjective implements Objective, VisitNewSiteObjective {

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
    final var value = solution.getVisitedSites().getSites().stream()
        .filter(Site::isEndangered).count();
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    return ObjectiveValue.builder().sense(sense).value(site.isEndangered() ? 1L : 0L).build();
  }
}
