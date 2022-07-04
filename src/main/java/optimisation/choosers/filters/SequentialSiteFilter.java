package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public class SequentialSiteFilter implements SiteFilter {

  @Singular private final List<SiteFilter> filters;

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    return filters.stream().allMatch(siteFilter -> siteFilter.siteHasInterest(solution, site));
  }
}
