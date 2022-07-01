package domain.objectives.components;

import domain.objectives.Objective;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.var;

/**
 * This class records the associated ObjectiveValue object of each contained Objective
 */
@EqualsAndHashCode
@Getter
public class ObjectiveValues {

  private final Map<Objective, ObjectiveValue> values;

  ObjectiveValues(final Map<Objective, ObjectiveValue> values) {
    this.values = values;
  }

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

  public static ObjectiveValuesBuilder builder() {
    return new ObjectiveValuesBuilder();
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
      values.put(objective, values.get(objective).sum(otherObjectiveValue));
    }
  }

  public ObjectiveValues copy() {
    final var copy = builder().build();
    for (final var entry : values.entrySet()) {
      copy.set(entry.getKey(), entry.getValue().copy());
    }
    return copy;
  }

  // Not using lombok builder because values can be empty and it does not work with Singular
  public static class ObjectiveValuesBuilder {
    private final Map<Objective, ObjectiveValue> values;

    ObjectiveValuesBuilder() {
      values = new HashMap<>();
    }

    public ObjectiveValuesBuilder value(
        final Objective objective, final ObjectiveValue objectiveValue) {
      values.put(objective, objectiveValue);
      return this;
    }

    public ObjectiveValues build() {
      return new ObjectiveValues(values);
    }
  }
}
