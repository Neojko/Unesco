package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;
import lombok.var;

/** Keeps sites whose type is the most represented in the solution */
public class UnderRepresentedSiteTypeFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    if (natural < cultural) {
      return site.isNatural();
    }
    if (cultural < natural) {
      return site.isCultural();
    }
    return true;
  }
}
