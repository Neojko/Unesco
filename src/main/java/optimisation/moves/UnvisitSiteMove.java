package optimisation.moves;

import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import lombok.Getter;

/**
 * UnvisitSiteMove is checking the Move attributes for unvisiting a site
 *
 * <p>We use a shortcut with isFeasible = true by nature because no currently implemented constraint
 * would become not feasible if we unvisit a site from a solution.
 */
@Getter
public class UnvisitSiteMove implements Move {

  private final Solution solution;
  private final Site site;
  private final long tripDurationDelta;
  private final ObjectiveValues objectiveValuesDelta;

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
      final ObjectiveManager objectiveManager) {
    this.solution = solution;
    this.site = site;
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
      final ObjectiveManager objectiveManager) {
    this.solution = solution;
    this.site = site;
    this.tripDurationDelta = tripDurationDelta;
    this.objectiveValuesDelta =
        objectiveManager.computeUnvisitSiteObjectiveValuesDelta(solution, site, tripDurationDelta);
  }

  // see class description
  @Override
  public boolean isFeasible() {
    return true;
  }
}
