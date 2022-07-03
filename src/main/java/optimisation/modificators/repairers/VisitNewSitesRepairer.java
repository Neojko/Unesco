package optimisation.modificators.repairers;

import domain.solution.Solution;
import lombok.Builder;
import optimisation.choosers.filters.SiteFilter;

@Builder
public class VisitNewSitesRepairer implements Repairer {

  private final SiteFilter filter;

  @Override
  public void repair(final Solution solution) {

  }
}
