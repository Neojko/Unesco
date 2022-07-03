package optimisation.modificators.repairers;

import domain.constraints.ConstraintManager;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.var;
import optimisation.choosers.filters.SiteFilter;
import optimisation.criteria.stopping.StoppingCriteria;
import optimisation.moves.VisitNewSiteMove;

@Builder
public class VisitNewSitesRepairer implements Repairer {

  private final StoppingCriteria stoppingCriteria;
  private final SiteFilter filter;

  @Override
  public void repair(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution
  ) {
    stoppingCriteria.initialise();
    while (!stoppingCriteria.isMet()) {
      final var bestMove = findBestMove(constraintManager, objectiveManager, matrix, solution);
      if (bestMove.isFeasible()) {
        solution.apply(bestMove);
        stoppingCriteria.update();
      }
      else {
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
      final Solution solution
  ) {
    VisitNewSiteMove bestMove = VisitNewSiteMove.createUnfeasibleMove();
    final var unvisitedSites = solution.getUnvisitedSites().getSites();
    final var unvisitedSitesOfInterest = filter.filter(solution, unvisitedSites);
    for (final var site : unvisitedSitesOfInterest) {
      final var pair = findShortestInsertionPosition(matrix, solution, site);
      final var move = new VisitNewSiteMove(solution, site, pair.getPosition(),
          pair.getTripDurationDelta(), constraintManager, objectiveManager);
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
      final TravelMatrix matrix,
      final Solution solution,
      final Site site
  ) {
    return IntStream.range(0, solution.getVisitedSites().getSites().size())
        .boxed()
        .map(position -> new PairPositionDelta(
            position,
            SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
                solution, site, position, matrix)))
        .min(PairPositionDelta::compareTo)
        .get();
  }

  /**
   * Mini-class to record the trip duration delta not to compute it again later on
   */
  @AllArgsConstructor
  @Getter
  private static class PairPositionDelta implements Comparable<PairPositionDelta> {
    private final int position;
    private final long tripDurationDelta;

    @Override
    public int compareTo(@NonNull final VisitNewSitesRepairer.PairPositionDelta other) {
      return Long.compare(tripDurationDelta, other.tripDurationDelta);
    }
  }
}
