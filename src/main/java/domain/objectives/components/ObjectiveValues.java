package domain.objectives.components;

import domain.objectives.Objective;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.var;

/** This class records the associated ObjectiveValue object of each contained Objective */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ObjectiveValues implements Comparable<ObjectiveValues> {

  private final Map<Objective, ObjectiveValue> values;
  private final ObjectiveRanking objectiveRanking;

  /** Initialises the lexicographic list of objectives and give them all a value 0 */
  public static ObjectiveValues createZeroObjectiveValues(final ObjectiveRanking objectiveRanking) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final Objective objective : objectiveRanking.getObjectives()) {
      objectiveValues.set(objective, objective.getZeroObjectiveValue());
    }
    return objectiveValues;
  }

  /** Initialises the lexicographic list of objectives and give them all the worst possible value */
  public static ObjectiveValues createWorstObjectiveValues(
      final ObjectiveRanking objectiveRanking) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final Objective objective : objectiveRanking.getObjectives()) {
      objectiveValues.set(objective, objective.getWorstObjectiveValue());
    }
    return objectiveValues;
  }

  public static ObjectiveValuesBuilder builder() {
    return new ObjectiveValuesBuilder();
  }

  /**
   * Update the value of an Objective in the map of values
   *
   * @param objective: must belong to objectiveRanking.getObjectives()
   * @param objectiveValue: must have the same sense as objective.getSense()
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
    final var copy = builder().ranking(objectiveRanking).build();
    for (final var entry : values.entrySet()) {
      copy.set(entry.getKey(), entry.getValue().copy());
    }
    return copy;
  }

  /**
   * @param other ObjectiveValues object to compare against; must have same ObjectiveRanking
   * @return -1 if this is better, 0 if same and -1 if other is better
   */
  @Override
  public int compareTo(@NonNull final ObjectiveValues other) {
    for (final var objective : objectiveRanking.getObjectives()) {
      final var objectiveComparison = values.get(objective).compareTo(other.values.get(objective));
      if (objectiveComparison != 0) {
        return objectiveComparison;
      }
    }
    return 0;
  }

  // Not using lombok builder because values can be empty and it does not work with Singular
  public static class ObjectiveValuesBuilder {
    private final Map<Objective, ObjectiveValue> values;
    private ObjectiveRanking objectiveRanking;

    ObjectiveValuesBuilder() {
      values = new HashMap<>();
    }

    public ObjectiveValuesBuilder value(
        final Objective objective, final ObjectiveValue objectiveValue) {
      values.put(objective, objectiveValue);
      return this;
    }

    public ObjectiveValuesBuilder ranking(final ObjectiveRanking objectiveRanking) {
      this.objectiveRanking = objectiveRanking;
      return this;
    }

    public ObjectiveValues build() {
      return new ObjectiveValues(values, objectiveRanking);
    }
  }
}
