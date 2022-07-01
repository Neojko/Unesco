package domain.objectives;

import domain.Solution;
import domain.objectives.components.ObjectiveValues;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.var;

/**
 * This class contains all Objectives of the problem and is used to compute everything that is
 * related to the ObjectiveValues of a Solution.
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
