package optimisation.choosers.selectors;

import domain.locations.sites.Site;
import java.util.List;
import lombok.AllArgsConstructor;
import util.RandomNumberGenerator;

@AllArgsConstructor
public class RandomSiteSelector implements SiteSelector {

  private final RandomNumberGenerator random;

  @Override
  public Site select(final List<Site> sites) {
    return sites.get(random.nextInt(sites.size()));
  }
}
