package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.UnvisitSiteObjective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.Getter;

public class TripRemainingTimeObjective
    implements Objective, VisitNewSiteObjective, UnvisitSiteObjective {

  private final ObjectiveSense sense;
  @Getter private final long maxTripDurationInSeconds;

  public TripRemainingTimeObjective(final long maxTripDurationInSeconds) {
    this.sense = ObjectiveSense.MAXIMIZE;
    this.maxTripDurationInSeconds = maxTripDurationInSeconds;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    return ObjectiveValue.builder()
        .sense(sense)
        .value(maxTripDurationInSeconds - solution.getTripDurationinSeconds())
        .build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    return ObjectiveValue.builder().sense(sense).value(-tripDurationDelta).build();
  }

  @Override
  public ObjectiveValue getUnvisitSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    return ObjectiveValue.builder().sense(sense).value(tripDurationDelta).build();
  }
}
