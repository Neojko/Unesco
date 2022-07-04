package domain.objectives.components;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveHolder.ObjectiveHolderBuilder;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.var;

@Getter
public class WeightedSumObjective implements Objective, VisitNewSiteObjective {

  private final ObjectiveSense sense;
  private final ObjectiveHolder objectiveHolder;
  private final Map<Objective, Long> weights;

  private WeightedSumObjective(
      final ObjectiveSense sense,
      final ObjectiveHolder objectiveHolder,
      final Map<Objective, Long> weights) {
    this.sense = sense;
    this.objectiveHolder = objectiveHolder;
    this.weights = weights;
  }

  public static WeightedSumObjectiveBuilder builder() {
    return new WeightedSumObjectiveBuilder();
  }

  public long getWeight(final Objective objective) {
    return weights.get(objective);
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    var objectiveValue = getZeroObjectiveValue();
    for (final var entry : weights.entrySet()) {
      final var objective = entry.getKey();
      final var weight = entry.getValue();
      objectiveValue =
          objectiveValue.sum(objective.computeObjectiveValue(solution).multiply(weight));
    }
    return objectiveValue;
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    var result = getZeroObjectiveValue();
    for (final var objective : objectiveHolder.getVisitNewSiteObjectives()) {
      final var weight = weights.get((Objective) objective);
      final var objectiveValue =
          objective.getVisitNewSiteObjectiveValueDelta(solution, site, tripDurationDelta);
      result = result.sum(objectiveValue.multiply(weight));
    }
    return result;
  }

  public static class WeightedSumObjectiveBuilder {
    private ObjectiveSense sense;
    private ObjectiveHolderBuilder objectiveHolderBuilder;
    private final Map<Objective, Long> weights;

    WeightedSumObjectiveBuilder() {
      objectiveHolderBuilder = ObjectiveHolder.builder();
      weights = new HashMap<>();
    }

    WeightedSumObjectiveBuilder sense(final ObjectiveSense sense) {
      this.sense = sense;
      return this;
    }

    WeightedSumObjectiveBuilder objective(final Objective objective, final long weight) {
      objectiveHolderBuilder = objectiveHolderBuilder.objective(objective);
      weights.put(objective, weight);
      return this;
    }

    WeightedSumObjective build() {
      return new WeightedSumObjective(sense, objectiveHolderBuilder.build(), weights);
    }
  }
}
