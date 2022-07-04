package domain.objectives.interfaces;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveValue;
import domain.solution.Solution;

public interface UnvisitSiteObjective {

  /**
   * @param solution: solution in which the site would be unvisited
   * @param site: site that would be unvisited
   * @param tripDurationDelta: solution trip duration delta if site is unvisited
   * @return delta of the ObjectiveValues of the Solution if site is unvisited
   */
  ObjectiveValue getUnvisitSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta);
}
