package optimisation.choosers.filters;

import domain.Solution;
import domain.locations.sites.Site;
import lombok.var;

/**
 * Keeps sites whose type is the most represented in the solution
 */

public class UnderRepresentedSiteTypeFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    final var cultural = solution.getNumberOfCulturalVisitedSites();
    final var natural = solution.getNumberOfNaturalVisitedSites();
    if (natural < cultural) {
      return site.isNatural();
    }
    if (cultural < natural) {
      return site.isCultural();
    }
    return true;
  }

}
