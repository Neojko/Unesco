package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveHolder;
import domain.objectives.components.ObjectiveHolder.ObjectiveHolderBuilder;
import domain.objectives.components.ObjectiveRanking;
import domain.objectives.components.ObjectiveValues;
import domain.objectives.interfaces.Objective;
import domain.solution.Solution;
import lombok.Getter;
import lombok.var;

/**
 * This class aims at computing the ObjectiveValues of a Solution and the ObjectiveValues delta of
 * any kind of Move. It contains all relevant Objective objects as well as their ranking. It acts as
 * a bridge between the Solution and its ObjectiveValues (that it computes).
 */
@Getter
public class ObjectiveManager {

  private final ObjectiveHolder objectiveHolder;
  private final ObjectiveRanking objectiveRanking;

  private ObjectiveManager(
      final ObjectiveHolder objectiveHolder, final ObjectiveRanking objectiveRanking) {
    this.objectiveHolder = objectiveHolder;
    this.objectiveRanking = objectiveRanking;
  }

  public static ObjectiveManagerBuilder builder() {
    return new ObjectiveManagerBuilder();
  }

  public ObjectiveValues computeObjectiveValues(final Solution solution) {
    final var objectiveValues = ObjectiveValues.builder().ranking(objectiveRanking).build();
    for (final var objective : objectiveHolder.getObjectives()) {
      objectiveValues.set(objective, objective.computeObjectiveValue(solution));
    }
    return objectiveValues;
  }

  public ObjectiveValues computeVisitNewSiteObjectiveValuesDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    final var objectiveValues = ObjectiveValues.builder().ranking(objectiveRanking).build();
    for (final var objective : objectiveHolder.getVisitNewSiteObjectives()) {
      objectiveValues.set(
          (Objective) objective,
          objective.getVisitNewSiteObjectiveValueDelta(solution, site, tripDurationDelta));
    }
    return objectiveValues;
  }

  public ObjectiveValues computeUnvisitSiteObjectiveValuesDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    final var objectiveValues = ObjectiveValues.builder().ranking(objectiveRanking).build();
    for (final var objective : objectiveHolder.getUnvisitSiteObjectives()) {
      objectiveValues.set(
          (Objective) objective,
          objective.getUnvisitSiteObjectiveValueDelta(solution, site, tripDurationDelta));
    }
    return objectiveValues;
  }

  public static class ObjectiveManagerBuilder {
    private ObjectiveHolderBuilder objectiveHolderBuilder;
    private ObjectiveRanking.ObjectiveRankingBuilder objectiveRankingBuilder;

    public ObjectiveManagerBuilder() {
      objectiveHolderBuilder = ObjectiveHolder.builder();
      objectiveRankingBuilder = ObjectiveRanking.builder();
    }

    public ObjectiveManagerBuilder objective(final Objective objective) {
      objectiveHolderBuilder = objectiveHolderBuilder.objective(objective);
      objectiveRankingBuilder = objectiveRankingBuilder.objective(objective);
      return this;
    }

    public ObjectiveManager build() {
      return new ObjectiveManager(objectiveHolderBuilder.build(), objectiveRankingBuilder.build());
    }
  }
}
