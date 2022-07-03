package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;
import lombok.var;

public class SiteWithNotVisitedCountriesFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    for (final var country : site.getCountries()) {
      if (!solution.getVisitedSites().containsCountry(country)) {
        return true;
      }
    }
    return false;
  }
}
