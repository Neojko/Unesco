package optimisation.modificators.destroyers;

import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.var;
import optimisation.choosers.filters.SiteFilter;
import optimisation.choosers.selectors.SiteSelector;
import optimisation.moves.UnvisitSiteMove;

@Getter
@Builder
public class RandomSiteDestroyer implements Destroyer {

  private final double percentage;
  private final SiteFilter filter;
  private final SiteSelector selector;

  /**
   * Unvisit sites that can be unvisited (rel. to constraintManager) until a given percentage has
   * been removed or there is no site that we can unvisit rel. to constraintManager and filter
   */
  @Override
  public void destroy(
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix,
      final Solution solution) {
    final var visitedSites = solution.getVisitedSites().getSites();
    final int numberOfSitesToUnvisit = computeNumberOfSitesToUnvisit(visitedSites.size());
    int currentNumberOfUnvisitedSites = 0;
    while (currentNumberOfUnvisitedSites < numberOfSitesToUnvisit) {
      final var sitesThatCanBeUnvisited =
          visitedSites.stream()
              .filter(site -> filter.siteHasInterest(solution, site))
              .filter(site -> constraintManager.canUnvisitSite(solution, site))
              .collect(Collectors.toList());
      if (sitesThatCanBeUnvisited.isEmpty()) {
        break;
      }
      final var toUnvisit = selector.select(sitesThatCanBeUnvisited);
      final var move =
          new UnvisitSiteMove(solution, toUnvisit, matrix, constraintManager, objectiveManager);
      solution.apply(move);
      sitesThatCanBeUnvisited.remove(toUnvisit);
      currentNumberOfUnvisitedSites++;
    }
  }

  private int computeNumberOfSitesToUnvisit(final int numberOfSites) {
    return (int) Math.round(percentage * numberOfSites);
  }
}
