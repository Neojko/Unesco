package domain.objectives;

import domain.Solution;

/**
 * An Objective object represents an objective in Operational Research. It has a sense (maximise or
 * minimise) and a value. In this project, we model all problem objectives with classes that
 * implement this interface. A Solution will then store ObjectiveValues to record its quality.
 */
public interface Objective {

  ObjectiveName getObjectiveName();

  ObjectiveSense getObjectiveSense();

  /*
   * @return ObjectiveValue with value 0 and same sense as the Objective one
   */
  default ObjectiveValue getZeroObjectiveValue() {
    return getObjectiveSense().equals(ObjectiveSense.MAXIMIZE)
        ? ObjectiveValue.ZERO_MAX_OBJECTIVE_VALUE
        : ObjectiveValue.ZERO_MIN_OBJECTIVE_VALUE;
  }

  /*
   * @return ObjectiveValue with worth possible value relatively to the Objective one and same sense
   */
  default ObjectiveValue getWorstObjectiveValue() {
    return getObjectiveSense().equals(ObjectiveSense.MAXIMIZE)
        ? ObjectiveValue.WORST_MAX_OBJECTIVE_VALUE
        : ObjectiveValue.WORST_MIN_OBJECTIVE_VALUE;
  }

  /** @return the ObjectiveValue of the Objective relatively to the given Solution */
  ObjectiveValue computeObjectiveValue(final Solution solution);
}
