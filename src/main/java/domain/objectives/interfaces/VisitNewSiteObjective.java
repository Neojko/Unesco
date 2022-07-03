package domain.objectives.interfaces;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveValue;
import domain.solution.Solution;

public interface VisitNewSiteObjective {

  /**
   * @param solution: solution in which the site would be visited
   * @param site: site that would be visited
   * @param tripDurationDelta: solution trip duration delta
   * @return delta of the ObjectiveValues of the Solution if site is visited
   */
  ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta);
}
