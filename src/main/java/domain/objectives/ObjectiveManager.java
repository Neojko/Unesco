package domain.objectives;

import domain.Solution;
import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveValues;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.var;

/**
 * This class contains all Objectives of the problem and is used to compute everything that is
 * related to the ObjectiveValues of a Solution.
 */
@Getter
public class ObjectiveManager {

  private final List<Objective> objectives;
  private final List<VisitNewSiteObjective> visitNewSiteObjectives;

  private ObjectiveManager(
      final List<Objective> objectives, final List<VisitNewSiteObjective> visitNewSiteObjectives) {
    this.objectives = objectives;
    this.visitNewSiteObjectives = visitNewSiteObjectives;
  }

  public static ObjectiveManagerBuilder builder() {
    return new ObjectiveManagerBuilder();
  }

  public ObjectiveValues computeObjectiveValues(final Solution solution) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final var objective : objectives) {
      objectiveValues.set(objective, objective.computeObjectiveValue(solution));
    }
    return objectiveValues;
  }

  public ObjectiveValues computeVisitNewSiteObjectiveValuesDelta(
      final Solution solution, final Site site) {
    final var objectiveValues = ObjectiveValues.builder().build();
    for (final var objective : visitNewSiteObjectives) {
      objectiveValues.set(
          (Objective) objective, objective.getVisitNewSiteObjectiveValueDelta(solution, site));
    }
    return objectiveValues;
  }

  public static class ObjectiveManagerBuilder {
    private final List<Objective> objectives;
    private final List<VisitNewSiteObjective> visitNewSiteObjectives;

    public ObjectiveManagerBuilder() {
      objectives = new ArrayList<>();
      visitNewSiteObjectives = new ArrayList<>();
    }

    public ObjectiveManagerBuilder objective(final Objective objective) {
      objectives.add(objective);
      if (objective instanceof VisitNewSiteObjective) {
        visitNewSiteObjectives.add((VisitNewSiteObjective) objective);
      }
      return this;
    }

    public ObjectiveManager build() {
      return new ObjectiveManager(objectives, visitNewSiteObjectives);
    }
  }
}
