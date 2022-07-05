package domain.constraints.interfaces;

import domain.locations.sites.Site;
import domain.solution.Solution;

public interface UnvisitSiteConstraint {

  boolean canUnvisitSite(final Solution solution, final Site site);
}
