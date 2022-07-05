package optimisation.moves;

import domain.constraints.ConstraintManager;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import lombok.Getter;

/** UnvisitSiteMove is checking the Move attributes for unvisiting a site */
@Getter
public class UnvisitSiteMove implements Move {

  private final Solution solution;
  private final Site site;
  private final boolean isFeasible;
  private final long tripDurationDelta;
  private final ObjectiveValues objectiveValuesDelta;

  public static UnvisitSiteMove createUnfeasibleMove() {
    return new UnvisitSiteMove();
  }

  /**
   * Main constructor -- we need to compute the value of tripDurationDelta
   *
   * @param solution: solution to simulate the move on
   * @param site: site to unvisit
   */
  public UnvisitSiteMove(
      final Solution solution,
      final Site site,
      final TravelMatrix matrix,
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager) {
    this.solution = solution;
    this.site = site;
    this.isFeasible = constraintManager.canUnvisitSite(solution, site);
    this.tripDurationDelta =
        SolutionTripDurationComputer.computeTripDurationDeltaToUnvisitSite(solution, site, matrix);
    this.objectiveValuesDelta =
        objectiveManager.computeUnvisitSiteObjectiveValuesDelta(solution, site, tripDurationDelta);
  }

  /**
   * Main constructor -- we know the value of tripDurationDelta
   *
   * @param solution: solution to simulate the move on
   * @param site: site to unvisit
   */
  public UnvisitSiteMove(
      final Solution solution,
      final Site site,
      final long tripDurationDelta,
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager) {
    this.solution = solution;
    this.site = site;
    this.isFeasible = constraintManager.canUnvisitSite(solution, site);
    this.tripDurationDelta = tripDurationDelta;
    this.objectiveValuesDelta =
        objectiveManager.computeUnvisitSiteObjectiveValuesDelta(solution, site, tripDurationDelta);
  }

  /** Creates unfeasible move -- no field should never be accessed apart from isFeasible */
  private UnvisitSiteMove() {
    this.solution = null;
    this.site = null;
    this.tripDurationDelta = 0;
    this.isFeasible = false;
    this.objectiveValuesDelta = null;
  }
}
