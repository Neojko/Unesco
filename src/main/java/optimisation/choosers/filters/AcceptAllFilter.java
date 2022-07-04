package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.ArrayList;
import java.util.List;

public class AcceptAllFilter implements SiteFilter {

  @Override
  public boolean siteHasInterest(final Solution solution, final Site site) {
    return true;
  }

  @Override
  public List<Site> filter(final Solution solution, final List<Site> sites) {
    return new ArrayList<>(sites);
  }
}
