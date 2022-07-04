package optimisation.modificators.repairers;

import domain.constraints.ConstraintManager;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.var;
import optimisation.choosers.filters.SiteFilter;
import optimisation.criteria.stopping.StoppingCriterion;
import optimisation.moves.VisitNewSiteMove;

/**
 * Aims to add the most interesting site to the list of visited sites at each iteration
 */

@Builder
public class BestVisitNewSitesRepairer implements Repairer {

  private final StoppingCriterion stoppingCriterion;
  private final SiteFilter filter;

  @Override
  public void repair(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution) {
    stoppingCriterion.initialise();
    while (!stoppingCriterion.isMet()) {
      final var bestMove = findBestMove(constraintManager, objectiveManager, matrix, solution);
      if (bestMove.isFeasible()) {
        solution.apply(bestMove);
        stoppingCriterion.update();
      } else {
        break;
      }
    }
  }

  /**
   * Computes best VisitNewSite move for all sites of interest (rel. to filter) and return best move
   */
  private VisitNewSiteMove findBestMove(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution) {
    VisitNewSiteMove bestMove = VisitNewSiteMove.createUnfeasibleMove();
    final var unvisitedSites = solution.getUnvisitedSites().getSites();
    final var unvisitedSitesOfInterest = filter.filter(solution, unvisitedSites);
    for (final var site : unvisitedSitesOfInterest) {
      final var pair = findShortestInsertionPosition(matrix, solution, site);
      final var move =
          new VisitNewSiteMove(
              solution,
              site,
              pair.getPosition(),
              pair.getTripDurationDelta(),
              constraintManager,
              objectiveManager);
      if (move.isBetterThan(bestMove)) {
        bestMove = move;
      }
    }
    return bestMove;
  }

  /**
   * Finds best position for a VisitNewSiteMove and also records the trip duration delta not to
   * compute it again in the move
   */
  final PairPositionDelta findShortestInsertionPosition(
      final TravelMatrix matrix, final Solution solution, final Site site) {
    var bestPair =
        new PairPositionDelta(
            0,
            SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
                solution, site, 0, matrix));
    if (solution.getVisitedSites().getSites().isEmpty()) {
      return bestPair;
    }
    for (int i = 1; i <= solution.getVisitedSites().getSites().size(); i++) {
      final long delta =
          SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
              solution, site, i, matrix);
      if (delta < bestPair.getTripDurationDelta()) {
        bestPair = new PairPositionDelta(i, delta);
      }
    }
    return bestPair;
  }

  /** Mini-class to record the trip duration delta not to compute it again later on */
  @AllArgsConstructor
  @Getter
  private static class PairPositionDelta {
    private final int position;
    private final long tripDurationDelta;
  }
}
