package domain.objectives.components;

import domain.Solution;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.var;

@Builder
@Getter
public class WeightedSumObjective implements Objective {

  private final ObjectiveSense sense;
  @Singular private final Map<Objective, Long> weights;

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
}
