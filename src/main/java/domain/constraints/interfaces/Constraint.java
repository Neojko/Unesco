package domain.constraints.interfaces;

import domain.solution.Solution;

public interface Constraint {

  boolean isFeasible(final Solution solution);
}
