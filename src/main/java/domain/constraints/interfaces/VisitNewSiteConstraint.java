package domain.constraints.interfaces;

import domain.locations.sites.Site;
import domain.solution.Solution;

public interface VisitNewSiteConstraint {
  boolean canVisitNewSite(
      final Solution solution,
      final Site site,
      final int position,
      final long tripDurationIncrease);
}
