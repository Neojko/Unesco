package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.solution.Solution;
import lombok.var;

/**
 * Keeps sites whose type is the most represented in the solution
 */

public class OverRepresentedSiteTypeFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    if (natural < cultural) {
      return site.getType().equals(SiteType.Cultural); // not taking mixed into account
    }
    if (cultural < natural) {
      return site.getType().equals(SiteType.Natural); // not taking mixed into account
    }
    return true;
  }

}
