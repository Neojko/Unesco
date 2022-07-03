package optimisation.moves;

import domain.constraints.ConstraintManager;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import lombok.Getter;

@Getter
public class VisitNewSiteMove implements Move {

  private final Solution solution;
  private final Site site;
  private final int position;
  private final long tripDurationDelta;
  private final boolean isFeasible;
  private final ObjectiveValues objectiveValuesDelta;

  public VisitNewSiteMove(
      final Solution solution,
      final Site site,
      final int position,
      final TravelMatrix matrix,
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager) {
    this.solution = solution;
    this.site = site;
    this.position = position;
    this.tripDurationDelta =
        SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
            solution, site, position, matrix);
    isFeasible = constraintManager.canVisitNewSite(solution, site, position, tripDurationDelta);
    objectiveValuesDelta =
        objectiveManager.computeVisitNewSiteObjectiveValuesDelta(solution, site, tripDurationDelta);
  }
}
