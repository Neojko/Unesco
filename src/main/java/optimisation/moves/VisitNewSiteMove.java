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

  public static VisitNewSiteMove createUnfeasibleMove() {
    return new VisitNewSiteMove();
  }

  /**
   * Main constructor -- we need to compute the value of tripDurationDelta
   * @param solution: solution to simulate the move on
   * @param site: site to visit
   * @param position: position to insert site
   */
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

  /**
   * Main constructor -- the value of tripDurationDelta is known
   * @param solution: solution to simulate the move on
   * @param site: site to visit
   * @param position: position to insert site
   */
  public VisitNewSiteMove(
      final Solution solution,
      final Site site,
      final int position,
      final long tripDurationDelta,
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager) {
    this.solution = solution;
    this.site = site;
    this.position = position;
    this.tripDurationDelta = tripDurationDelta;
    isFeasible = constraintManager.canVisitNewSite(solution, site, position, tripDurationDelta);
    objectiveValuesDelta =
        objectiveManager.computeVisitNewSiteObjectiveValuesDelta(solution, site, tripDurationDelta);
  }

  /**
   * Creates unfeasible move -- no field should never be accessed apart from isFeasible
   */
  private VisitNewSiteMove() {
    this.solution = null;
    this.site = null;
    this.position = 0;
    this.tripDurationDelta = 0;
    this.isFeasible = false;
    this.objectiveValuesDelta = null;
  }
}
