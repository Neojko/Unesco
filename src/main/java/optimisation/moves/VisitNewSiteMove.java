package optimisation.moves;

import domain.Solution;
import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import domain.site.Site;
import lombok.Getter;

@Getter
public class VisitNewSiteMove implements Move {

  private final Solution solution;
  private final Site site;
  private final int position;
  private final boolean isFeasible;
  private final ObjectiveValues objectiveValuesDelta;

  public VisitNewSiteMove(
      final Solution solution,
      final Site site,
      final int position,
      final TravelMatrix matrix,
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager
  ) {
    this.solution = solution;
    this.site = site;
    this.position = position;
    isFeasible = constraintManager.canVisitNewSite(solution, site, position, matrix);
    objectiveValuesDelta = objectiveManager.computeVisitNewSiteObjectiveValuesDelta(solution, site);
  }

}
