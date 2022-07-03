package optimisation.criteria.acceptance;

import domain.solution.Solution;

/**
 * Determines whether we should use a newly found solution as the current solution in local search
 * operators
 */

public interface SolutionAcceptanceCriterion {

  default void initialise(final Solution initialSolution) {}

  boolean accept(Solution newSolution, Solution currentSolution);

  default void update(final Solution solution) {}

}
