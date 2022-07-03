package domain.constraints;

import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

@Getter
@AllArgsConstructor
public class ConstraintManager implements Constraint, VisitNewSiteConstraint {

  final List<Constraint> constraints;
  final List<VisitNewSiteConstraint> visitNewSiteConstraints;

  public static ConstraintManagerBuilder builder() {
    return new ConstraintManagerBuilder();
  }

  @Override
  public boolean isFeasible(final Solution solution) {
    for (final var constraint : constraints) {
      if (!constraint.isFeasible(solution)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean canVisitNewSite(
      final Solution solution,
      final Site site,
      final int position,
      final long tripDurationIncrease) {
    for (final var constraint : visitNewSiteConstraints) {
      if (!constraint.canVisitNewSite(solution, site, position, tripDurationIncrease)) {
        return false;
      }
    }
    return true;
  }

  public static class ConstraintManagerBuilder {
    final List<Constraint> constraints;
    final List<VisitNewSiteConstraint> visitNewSiteConstraints;

    public ConstraintManagerBuilder() {
      constraints = new ArrayList<>();
      visitNewSiteConstraints = new ArrayList<>();
    }

    public ConstraintManagerBuilder constraint(final Constraint constraint) {
      constraints.add(constraint);
      if (constraint instanceof VisitNewSiteConstraint) {
        visitNewSiteConstraints.add((VisitNewSiteConstraint) constraint);
      }
      return this;
    }

    public ConstraintManager build() {
      return new ConstraintManager(constraints, visitNewSiteConstraints);
    }
  }
}
