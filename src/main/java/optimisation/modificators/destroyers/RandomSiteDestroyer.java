package optimisation.modificators.destroyers;

import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
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

  @Override
  public void destroy(
      final ObjectiveManager objectiveManager, final TravelMatrix matrix, final Solution solution) {
    final var unvisitableSites = filter.filter(solution, solution.getVisitedSites().getSites());
    final int numberOfSitesToUnvisit = computeNumberOfSitesToUnvisit(unvisitableSites.size());
    int currentNumberOfUnvisitedSites = 0;
    while (currentNumberOfUnvisitedSites < numberOfSitesToUnvisit) {
      final var toUnvisit = selector.select(unvisitableSites);
      final var move = new UnvisitSiteMove(solution, toUnvisit, matrix, objectiveManager);
      solution.apply(move);
      unvisitableSites.remove(toUnvisit);
      currentNumberOfUnvisitedSites++;
    }
  }

  private int computeNumberOfSitesToUnvisit(final int numberOfSites) {
    return (int) Math.round(percentage * numberOfSites);
  }
}
