package optimisation;

import domain.Instance;
import domain.constraints.ConstraintManager;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import lombok.Builder;
import lombok.Getter;
import lombok.var;
import optimisation.algorithms.Algorithm;
import optimisation.modificators.PostProcesser;
import optimisation.modificators.repairers.Repairer;

/** A Solver produces an initial Solution from an Instance and run an Algorithm on it */
@Builder
@Getter
public class Solver {

  final ConstraintManager constraintManager;
  final ObjectiveManager objectiveManager;
  final Instance instance;
  final Algorithm algorithm;
  final Repairer initialRepairer;

  public Solution solve() {
    final var initialSolution = createInitialSolution();
    final var matrix = instance.getMatrix();
    final var solution =
        algorithm.improve(constraintManager, objectiveManager, matrix, initialSolution);
    PostProcesser.fix(constraintManager, objectiveManager, matrix, solution);
    return solution;
  }

  private Solution createInitialSolution() {
    final var initialSolution = createEmptySolution();
    final var matrix = instance.getMatrix();
    initialRepairer.repair(constraintManager, objectiveManager, matrix, initialSolution);
    return initialSolution;
  }

  private Solution createEmptySolution() {
    var solutionBuilder = Solution.builder().start(instance.getStart());
    for (final var site : instance.getSites()) {
      solutionBuilder = solutionBuilder.unvisitedSite(site);
    }
    return solutionBuilder.build(constraintManager, objectiveManager, instance.getMatrix());
  }
}
