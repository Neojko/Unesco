package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.UnvisitSiteObjective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;

public class NumberOfVisitedSitesObjective implements
    Objective, VisitNewSiteObjective, UnvisitSiteObjective {

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
    return ObjectiveValue.builder()
        .sense(sense)
        .value(solution.getVisitedSites().getSites().size())
        .build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    return ObjectiveValue.builder().sense(sense).value(1L).build();
  }

  @Override
  public ObjectiveValue getUnvisitSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    return ObjectiveValue.builder().sense(sense).value(-1L).build();
  }
}
