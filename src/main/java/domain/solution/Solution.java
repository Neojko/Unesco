package domain.solution;

import domain.constraints.ConstraintManager;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.locations.sites.Sites;
import domain.locations.sites.Sites.SitesBuilder;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import optimisation.moves.UnvisitSiteMove;
import optimisation.moves.VisitNewSiteMove;

@EqualsAndHashCode
@Getter
public class Solution {

  private final TravelStartLocation start;
  private final Sites visitedSites;
  private final Sites unvisitedSites;
  private final boolean isFeasible;
  private final ObjectiveValues objectiveValues;
  private long tripDurationinSeconds;

  // Constructor that is used by SolutionBuilder
  private Solution(
      final TravelStartLocation start,
      final Sites visitedSites,
      final Sites unvisitedSites,
      final ConstraintManager constraintManager,
      final ObjectiveManager objectiveManager,
      final TravelMatrix matrix) {
    this.start = start;
    this.visitedSites = visitedSites;
    this.unvisitedSites = unvisitedSites;
    this.isFeasible = constraintManager.isFeasible(this);
    this.objectiveValues = objectiveManager.computeObjectiveValues(this);
    this.tripDurationinSeconds = SolutionTripDurationComputer.computeTripDuration(this, matrix);
  }

  // Constructor that is used by copy()
  private Solution(
      final TravelStartLocation start,
      final Sites visitedSites,
      final Sites unvisitedSites,
      final boolean isFeasible,
      final ObjectiveValues objectiveValues,
      final long tripDurationinSeconds) {
    this.start = start;
    this.visitedSites = visitedSites;
    this.unvisitedSites = unvisitedSites;
    this.isFeasible = isFeasible;
    this.objectiveValues = objectiveValues;
    this.tripDurationinSeconds = tripDurationinSeconds;
  }

  public static SolutionBuilder builder() {
    return new SolutionBuilder();
  }

  public Solution copy() {
    return new Solution(
        start,
        visitedSites.copy(),
        unvisitedSites.copy(),
        isFeasible,
        objectiveValues.copy(),
        tripDurationinSeconds);
  }

  public boolean isBetterThan(final Solution other) {
    if (!isFeasible()) {
      return false;
    }
    if (!other.isFeasible()) {
      return true;
    }
    return getObjectiveValues().compareTo(other.getObjectiveValues()) < 0;
  }

  public void apply(final VisitNewSiteMove move) {
    visitedSites.addSite(move.getSite(), move.getPosition());
    unvisitedSites.removeSite(move.getSite());
    objectiveValues.add(move.getObjectiveValuesDelta());
    tripDurationinSeconds += move.getTripDurationDelta();
  }

  public void apply(final UnvisitSiteMove move) {
    visitedSites.removeSite(move.getSite());
    unvisitedSites.addSite(move.getSite());
    objectiveValues.add(move.getObjectiveValuesDelta());
    tripDurationinSeconds += move.getTripDurationDelta();
  }

  public static class SolutionBuilder {
    private TravelStartLocation start;
    private SitesBuilder visitedSitesBuilder;
    private SitesBuilder unvisitedSitesBuilder;

    public SolutionBuilder() {
      visitedSitesBuilder = Sites.builder();
      unvisitedSitesBuilder = Sites.builder();
    }

    public SolutionBuilder start(final TravelStartLocation start) {
      this.start = start;
      return this;
    }

    public SolutionBuilder visitedSite(final Site visitedSite) {
      visitedSitesBuilder = visitedSitesBuilder.site(visitedSite);
      return this;
    }

    public SolutionBuilder unvisitedSite(final Site unvisitedSite) {
      unvisitedSitesBuilder = unvisitedSitesBuilder.site(unvisitedSite);
      return this;
    }

    public Solution build(
        final ConstraintManager constraintManager,
        final ObjectiveManager objectiveManager,
        final TravelMatrix matrix) {
      return new Solution(
          start,
          visitedSitesBuilder.build(),
          unvisitedSitesBuilder.build(),
          constraintManager,
          objectiveManager,
          matrix);
    }

    public Solution build(final TravelMatrix matrix) {
      return new Solution(
          start,
          visitedSitesBuilder.build(),
          unvisitedSitesBuilder.build(),
          ConstraintManager.builder().build(),
          ObjectiveManager.builder().build(),
          matrix);
    }
  }
}
