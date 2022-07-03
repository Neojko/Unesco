package optimisation.modificators.repairers;

import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;

/**
 * A Repairer is in charge of constructing a Solution. For the Unesco problem, it will try to add
 * sites to visit to the given Solution
 */
public interface Repairer {

  void repair(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution);
}
