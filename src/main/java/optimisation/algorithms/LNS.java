package optimisation.algorithms;

import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import lombok.Builder;
import lombok.Getter;
import lombok.var;
import optimisation.criteria.acceptance.SolutionAcceptanceCriterion;
import optimisation.criteria.stopping.StoppingCriterion;
import optimisation.modificators.destroyers.Destroyer;
import optimisation.modificators.repairers.Repairer;

/**
 * LNS stands for Large Neighborhood Search. It's a metaheuristic that iteratively destroy and
 * repair a solution. At the end of each iteration, we can use a solution acceptance criterion to
 * determine which solution should the next current solution be.
 */
@Builder
@Getter
public class LNS implements Algorithm {

  private final Destroyer destroyer;
  private final Repairer repairer;
  private final SolutionAcceptanceCriterion acceptanceCriterion;
  private final StoppingCriterion stoppingCriterion;

  @Override
  public Solution improve(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution) {
    var bestSolution = solution.copy();
    var currentSolution = solution.copy();
    stoppingCriterion.initialise();
    while (!stoppingCriterion.isMet()) {
      destroyer.destroy(objectiveManager, matrix, solution);
      repairer.repair(constraintManager, objectiveManager, matrix, solution);
      if (currentSolution.isBetterThan(bestSolution)) {
        bestSolution = currentSolution.copy();
      } else if (!acceptanceCriterion.accept(currentSolution, bestSolution)) {
        currentSolution = bestSolution.copy();
      }
      stoppingCriterion.update();
    }
    return bestSolution;
  }
}
