package optimisation.algorithms;

import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;

public interface Algorithm {

  Solution improve(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution);

}
