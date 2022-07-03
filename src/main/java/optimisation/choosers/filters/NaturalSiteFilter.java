package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;

public class NaturalSiteFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    return site.isNatural();
  }
}
