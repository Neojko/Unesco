package domain.objectives.interfaces;

import domain.Solution;
import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveValue;

public interface VisitNewSiteObjective {

  /**
   * @param solution: solution in which the site would be visited
   * @param site: site that would be visited
   * @return delta of the ObjectiveValues of the Solution if site was visited
   */
  ObjectiveValue getVisitNewSiteObjectiveValueDelta(final Solution solution, final Site site);
}