package optimisation.modificators.repairers;

import domain.Solution;

/**
 * A Repairer is in charge of constructing a Solution. For the Unesco problem, it will try to add
 * sites to visit to the given Solution
 */
public interface Repairer {



  void repair(final Solution solution);
}
