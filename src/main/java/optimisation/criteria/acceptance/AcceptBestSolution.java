package optimisation.criteria.acceptance;

import domain.solution.Solution;

public class AcceptBestSolution implements SolutionAcceptanceCriterion {

  @Override
  public boolean accept(final Solution newSolution, final Solution currentSolution) {
    return newSolution.isBetterThan(currentSolution);
  }

}
