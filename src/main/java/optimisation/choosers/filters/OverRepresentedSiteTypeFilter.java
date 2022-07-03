package optimisation.choosers.filters;

import domain.Solution;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import lombok.var;

public class OverRepresentedSiteTypeFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    final var cultural = solution.getNumberOfCulturalVisitedSites();
    final var natural = solution.getNumberOfNaturalVisitedSites();
    if (natural < cultural) {
      return site.getType().equals(SiteType.Cultural); // not taking mixed into account
    }
    if (cultural < natural) {
      return site.getType().equals(SiteType.Natural); // not taking mixed into account
    }
    return true;
  }

}
