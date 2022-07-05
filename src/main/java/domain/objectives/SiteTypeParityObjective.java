package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.UnvisitSiteObjective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.var;

/**
 * This Objective aims to minimise the absolute difference between natural and cultural sites. It is
 * implemented with sense MAXIMISE to integrate it into a weighted sum of other MAXIMIZE objectives.
 */
public class SiteTypeParityObjective
    implements Objective, VisitNewSiteObjective, UnvisitSiteObjective {

  private final ObjectiveSense sense;

  public SiteTypeParityObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    final var value = -getAbsoluteDifference(solution); // negative to maximise objective
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationIncrease) {
    final var oldValue = getAbsoluteDifference(solution);
    final var newValue =
        getAbsoluteDifference(solution, site.isCultural() ? 1 : 0, site.isNatural() ? 1 : 0);
    final var negativeValueDelta = oldValue - newValue; // = -(newValue - oldValue)
    return ObjectiveValue.builder().sense(sense).value(negativeValueDelta).build();
  }

  @Override
  public ObjectiveValue getUnvisitSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationIncrease) {
    final var oldValue = getAbsoluteDifference(solution);
    final var newValue =
        getAbsoluteDifference(solution, site.isCultural() ? -1 : 0, site.isNatural() ? -1 : 0);
    final var negativeValueDelta = oldValue - newValue; // = -(newValue - oldValue)
    return ObjectiveValue.builder().sense(sense).value(negativeValueDelta).build();
  }

  /**
   * Returns absolute difference between numbers of cultural sites and natural sites in solution
   */
  private int getAbsoluteDifference(final Solution solution) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    return Math.abs(cultural - natural);
  }

  /**
   * Returns absolute difference between numbers of cultural sites and natural sites in solution
   * with a delta change for cultural and natural sites.
   * @param solution: used to get initial number of cultural and natural sites
   * @param culturalDelta: cultural site delta
   * @param naturalDelta: natural site delta
   */
  private int getAbsoluteDifference(
      final Solution solution,
      final int culturalDelta,
      final int naturalDelta
  ) {
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites() + culturalDelta;
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites() + naturalDelta;
    return Math.abs(cultural - natural);
  }
}
