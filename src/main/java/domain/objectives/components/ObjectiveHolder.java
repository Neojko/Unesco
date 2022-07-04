package domain.objectives.components;

import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * This class is here to contain a list of Objective objects as well as lists of Objective objects
 * for all types of Objective
 */
@EqualsAndHashCode
@Getter
public class ObjectiveHolder {

  private final List<Objective> objectives;
  private final List<VisitNewSiteObjective> visitNewSiteObjectives;

  private ObjectiveHolder(
      final List<Objective> objectives, final List<VisitNewSiteObjective> visitNewSiteObjectives) {
    this.objectives = objectives;
    this.visitNewSiteObjectives = visitNewSiteObjectives;
  }

  public static ObjectiveHolderBuilder builder() {
    return new ObjectiveHolderBuilder();
  }

  public static class ObjectiveHolderBuilder {
    private final List<Objective> objectives;
    private final List<VisitNewSiteObjective> visitNewSiteObjectives;

    public ObjectiveHolderBuilder() {
      objectives = new ArrayList<>();
      visitNewSiteObjectives = new ArrayList<>();
    }

    public ObjectiveHolderBuilder objective(final Objective objective) {
      objectives.add(objective);
      if (objective instanceof VisitNewSiteObjective) {
        visitNewSiteObjectives.add((VisitNewSiteObjective) objective);
      }
      return this;
    }

    public ObjectiveHolder build() {
      return new ObjectiveHolder(objectives, visitNewSiteObjectives);
    }
  }
}
