package domain.objectives.components;

import domain.objectives.Objective;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.var;

/**
 * This class records the associated ObjectiveValue object of each contained Objective
 */
@EqualsAndHashCode
@Getter
@Builder
public class ObjectiveValues {

  @Singular private final Map<Objective, ObjectiveValue> values;

  /** Initialises the lexicographic list of objectives and give them all a value 0 */
  public static ObjectiveValues createZeroObjectiveValues(final List<Objective> objectives) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final Objective objective : objectives) {
      objectiveValues.set(objective, objective.getZeroObjectiveValue());
    }
    return objectiveValues;
  }

  /** Initialises the lexicographic list of objectives and give them all the worst possible value */
  public static ObjectiveValues createWorstObjectiveValues(final List<Objective> objectives) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final Objective objective : objectives) {
      objectiveValues.set(objective, objective.getWorstObjectiveValue());
    }
    return objectiveValues;
  }

  /**
   * Updating the ObjectiveValue of a contained Objective
   *
   * @param objective: needs to be contained in this
   */
  public void set(final Objective objective, final ObjectiveValue objectiveValue) {
    values.put(objective, objectiveValue);
  }

  /**
   * Updating the current ObjectiveValues by adding the other ObjectiveValues
   *
   * @param other: needs to have the same Objective as this
   */
  public void add(final ObjectiveValues other) {
    for (final var entry : other.getValues().entrySet()) {
      final var objective = entry.getKey();
      final var otherObjectiveValue = entry.getValue();
      values.put(objective, values.get(objective).plus(otherObjectiveValue));
    }
  }

  public ObjectiveValues copy() {
    final var copy = builder().build();
    for (final var entry : values.entrySet()) {
      copy.set(entry.getKey(), entry.getValue().copy());
    }
    return copy;
  }
}
