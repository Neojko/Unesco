package domain.constraints.interfaces;

import domain.Solution;

public interface Constraint {

  boolean isFeasible(final Solution solution);
}
