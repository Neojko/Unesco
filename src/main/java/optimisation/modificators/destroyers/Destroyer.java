package optimisation.modificators.destroyers;

import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;

/**
 * A Destroyer is in charge of removing some parts of a Solution. For the Unesco problem, it will
 * try to unvisit some sites from the given Solution
 */
public interface Destroyer {

  void destroy(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution);
}
