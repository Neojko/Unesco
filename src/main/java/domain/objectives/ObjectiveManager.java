package domain.objectives;

import domain.Solution;
import domain.objectives.components.ObjectiveValues;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.var;

/**
 * This class aggregates all Objectives and is in charge of comparing their related ObjectiveValues
 * relatively to a lexicographical order and / or a weighted sum fashion.
 * It acts as a bridge between a Solution and its ObjectiveValues.
 * Note: following the YAGNI principle, the way to compare ObjectiveValues is hard-coded as the
 * objectives as well as their respected ranking are known and won't change.
 */

@Builder
@Getter
public class ObjectiveManager {

  @Singular private final List<Objective> objectives;

  public ObjectiveValues computeObjectivesValues(final Solution solution) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final var objective : objectives) {
      objectiveValues.set(objective, objective.computeObjectiveValue(solution));
    }
    return objectiveValues;
  }

}
