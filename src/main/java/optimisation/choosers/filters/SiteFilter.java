package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.List;
import java.util.stream.Collectors;

public interface SiteFilter {

  boolean siteHasInterest(final Solution solution, final Site site);

  default List<Site> filter(final Solution solution, final List<Site> sites) {
    return sites.stream()
        .filter(site -> siteHasInterest(solution, site))
        .collect(Collectors.toList());
  }
}
