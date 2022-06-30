package domain.objectives.components;

import domain.objectives.Objective;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.var;

/**
 * The goal of this class is to : - record the associated ObjectiveValue object of each contained
 * Objective - compare itself against other ObjectiveValues
 */
@EqualsAndHashCode
@Getter
public class ObjectiveValues {

  private final Map<Objective, ObjectiveValue> objectiveValuesMap;
  private final List<Objective> objectives; // ordered list from most important to less important

  /** Initialises the lexicographic list of objectives but does not give them any ObjectiveValue */
  public static ObjectiveValues createEmptyObjectiveValues(final List<Objective> objectives) {
    return new ObjectiveValues(objectives);
  }

  /** Initialises the lexicographic list of objectives and give them all a value 0 */
  public static ObjectiveValues createZeroObjectiveValues(final List<Objective> objectives) {
    final var objectiveValues = new ObjectiveValues(objectives);
    for (final Objective objective : objectives) {
      objectiveValues.set(objective, objective.getZeroObjectiveValue());
    }
    return objectiveValues;
  }

  /** Initialises the lexicographic list of objectives and give them all the worst possible value */
  public static ObjectiveValues createWorstObjectiveValues(final List<Objective> objectives) {
    final var objectiveValues = new ObjectiveValues(objectives);
    for (final Objective objective : objectives) {
      objectiveValues.set(objective, objective.getWorstObjectiveValue());
    }
    return objectiveValues;
  }

  /** Used in all above public static constructors */
  private ObjectiveValues(final List<Objective> objectives) {
    objectiveValuesMap = new HashMap<>();
    this.objectives = objectives;
  }

  /**
   * Updating the ObjectiveValue of a contained Objective
   *
   * @param objective: needs to be contained in this
   */
  public void set(final Objective objective, final ObjectiveValue objectiveValue) {
    objectiveValuesMap.put(objective, objectiveValue);
  }

  /**
   * Updating the current ObjectiveValues by adding the other ObjectiveValues
   *
   * @param other: needs to have the same Objective as this
   */
  public void add(final ObjectiveValues other) {
    for (final Objective objective : other.objectives) {
      objectiveValuesMap.put(
          objective,
          objectiveValuesMap.get(objective).plus(other.objectiveValuesMap.get(objective)));
    }
  }

  /**
   * Compares two ObjectiveValues objects in the lexicographic order of their ObjectiveValue objects
   *
   * @param other: needs to have the same Objective objects as this
   * @return true if this is better than other, false otherwise (included equality)
   */
  public boolean isBetterThan(final ObjectiveValues other) {
    for (final Objective objective : objectives) {
      final ObjectiveValue objectiveValue = objectiveValuesMap.get(objective);
      final ObjectiveValue otherObjectiveValue = other.objectiveValuesMap.get(objective);
      if (objectiveValue.isBetterThan(otherObjectiveValue)) {
        return true;
      }
      if (otherObjectiveValue.isBetterThan(objectiveValue)) {
        return false;
      }
    }
    return false;
  }

  public ObjectiveValues copy() {
    final var copy = new ObjectiveValues(objectives);
    for (final var entry : objectiveValuesMap.entrySet()) {
      copy.set(entry.getKey(), entry.getValue().copy());
    }
    return copy;
  }
}
