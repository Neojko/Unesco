package optimisation.moves;

import domain.objectives.components.ObjectiveValues;

/**
 * A Move aims to compute the impact of a Solution modification without modifying the Solution. To
 * determine this impact, we must compute the feasibility of the Move (whether the Solution remains
 * feasible) and the delta on the ObjectiveValues of the Solution.
 */
public interface Move {

  boolean isFeasible();

  ObjectiveValues getObjectiveValuesDelta();

  default boolean isBetterThan(final Move other) {
    if (!isFeasible()) {
      return false;
    }
    if (!other.isFeasible()) {
      return true;
    }
    return getObjectiveValuesDelta().compareTo(other.getObjectiveValuesDelta()) < 0;
  }
}
