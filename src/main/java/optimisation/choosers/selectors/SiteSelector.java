package optimisation.choosers.selectors;

import domain.locations.sites.Site;
import java.util.List;

public interface SiteSelector {

  Site select(final List<Site> sites);

}
