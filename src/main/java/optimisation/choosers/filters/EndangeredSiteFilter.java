package optimisation.choosers.filters;

import domain.Solution;
import domain.locations.sites.Site;

public class EndangeredSiteFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    return site.isEndangered();
  }

}
