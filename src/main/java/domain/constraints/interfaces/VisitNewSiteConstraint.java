package domain.constraints.interfaces;

import domain.Solution;
import domain.matrix.TravelMatrix;
import domain.site.Site;

public interface VisitNewSiteConstraint {

  boolean canVisitNewSite(
      final Solution solution,
      final Site site,
      final int position,
      final TravelMatrix matrix);

}
