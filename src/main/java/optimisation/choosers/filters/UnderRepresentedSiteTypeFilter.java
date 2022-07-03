package optimisation.choosers.filters;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.List;
import java.util.stream.Collectors;
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

  @Override
  public List<Site> filter(final Solution solution, final List<Site> sites) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    if (natural < cultural) {
      return sites.stream()
          .filter(Site::isNatural) // not taking mixed into account
          .collect(Collectors.toList());
    }
    if (cultural < natural) {
      return sites.stream()
          .filter(Site::isCultural) // not taking mixed into account
          .collect(Collectors.toList());
    }
    return sites;
  }
}
