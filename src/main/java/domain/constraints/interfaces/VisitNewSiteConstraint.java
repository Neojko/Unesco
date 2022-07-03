package domain.constraints.interfaces;

import domain.Solution;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;

public interface VisitNewSiteConstraint {

  boolean canVisitNewSite(
      final Solution solution, final Site site, final int position, final TravelMatrix matrix);
}
