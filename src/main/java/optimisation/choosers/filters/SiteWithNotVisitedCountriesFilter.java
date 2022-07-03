package optimisation.choosers.filters;

import domain.Solution;
import domain.locations.sites.Site;
import lombok.var;

public class SiteWithNotVisitedCountriesFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    for (final var country : site.getCountries()) {
      if (!solution.getVisitedCountries().containsKey(country)) {
        return true;
      }
    }
    return false;
  }
}
