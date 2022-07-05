package optimisation.modificators;

import static domain.locations.sites.SiteType.Cultural;
import static domain.locations.sites.SiteType.Natural;

import domain.constraints.ConstraintManager;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import java.util.List;
import java.util.stream.Collectors;
import lombok.var;
import optimisation.moves.UnvisitSiteMove;

public class PostProcesser {

  public static void fix(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    if (cultural < natural) {
      final var numberToRemove = natural - cultural;
      removeSites(constraintManager, objectiveManager, matrix, solution, Natural, numberToRemove);
    } else if (natural < cultural) {
      final var numberToRemove = cultural - natural;
      removeSites(constraintManager, objectiveManager, matrix, solution, Cultural, numberToRemove);
    }
  }

  /** Removes sites of Solution such that it has same number of cultural and natural types */
  @SuppressWarnings("PMD") // PMD bug with static void that are considered as not used
  private static void removeSites(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution,
      final SiteType siteTypeToRemove,
      final int numberOfSitesToRemove) {
    int currentUnvisited = 0;
    final List<Site> sites = getPotentialSitesToUnvisit(solution, siteTypeToRemove);
    while (currentUnvisited < numberOfSitesToRemove) {
      UnvisitSiteMove bestMove = UnvisitSiteMove.createUnfeasibleMove();
      for (final var site : sites) {
        final var move =
            new UnvisitSiteMove(solution, site, matrix, constraintManager, objectiveManager);
        if (move.isBetterThan(bestMove)) {
          bestMove = move;
        }
      }
      solution.apply(bestMove);
      sites.remove(bestMove.getSite());
      currentUnvisited++;
    }
  }

  /**
   * Returns visited sites of solution of type siteType
   *
   * @param siteType: equals to Cultural or Natural (we don't want to remove Mixed sites)
   */
  private static List<Site> getPotentialSitesToUnvisit(
      final Solution solution, final SiteType siteType) {
    if (siteType.equals(Cultural)) {
      return solution.getVisitedSites().getSites().stream()
          .filter(site -> site.getType().equals(Cultural))
          .collect(Collectors.toList());
    }
    // siteType = Natural
    return solution.getVisitedSites().getSites().stream()
        .filter(site -> site.getType().equals(Natural))
        .collect(Collectors.toList());
  }
}
