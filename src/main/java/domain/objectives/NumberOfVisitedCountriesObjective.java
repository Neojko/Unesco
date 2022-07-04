package domain.objectives;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.UnvisitSiteObjective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.var;

public class NumberOfVisitedCountriesObjective
    implements Objective, VisitNewSiteObjective, UnvisitSiteObjective {

  private final ObjectiveSense sense;

  public NumberOfVisitedCountriesObjective() {
    this.sense = ObjectiveSense.MAXIMIZE;
  }

  @Override
  public ObjectiveSense getObjectiveSense() {
    return sense;
  }

  @Override
  public ObjectiveValue computeObjectiveValue(final Solution solution) {
    return ObjectiveValue.builder()
        .sense(sense)
        .value(solution.getVisitedSites().getSitesPerCountry().size())
        .build();
  }

  @Override
  public ObjectiveValue getVisitNewSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    final var value =
        site.getCountries().stream()
            .filter(country -> !solution.getVisitedSites().containsCountry(country))
            .count();
    return ObjectiveValue.builder().sense(sense).value(value).build();
  }

  @Override
  public ObjectiveValue getUnvisitSiteObjectiveValueDelta(
      final Solution solution, final Site site, final long tripDurationDelta) {
    final var sitesPerCountry = solution.getVisitedSites().getSitesPerCountry();
    final var value =
        site.getCountries().stream()
            .filter(country -> sitesPerCountry.get(country).size() == 1)
            .count();
    return ObjectiveValue.builder().sense(sense).value(-value).build();
  }
}
