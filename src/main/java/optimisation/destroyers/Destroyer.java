package optimisation.destroyers;

import domain.Solution;

/**
 * A Repairer is in charge of removing some parts of a Solution. For the Unesco problem, it will try
 * to unvisit some sites from the given Solution
 */
public interface Destroyer {

  void destroy(final Solution solution);
}
