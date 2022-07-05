package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveSense;
import domain.solution.Solution;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TripRemainingTimeObjectiveTest {

  private Solution solution;
  private Site site;
  private long tripDuration, tripDurationDelta, maxTripDuration;
  private TripRemainingTimeObjective objective;

  @BeforeEach
  public void setUp() {
    solution = mock(Solution.class);
    site = mock(Site.class);
    tripDuration = 8L;
    when(solution.getTripDurationinSeconds()).thenReturn(tripDuration);
    tripDurationDelta = 3L;
    maxTripDuration = 20L;
    objective = new TripRemainingTimeObjective(maxTripDuration);
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MAXIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_max_trip_duration() {
    assertEquals(maxTripDuration, objective.getMaxTripDurationInSeconds());
  }

  @Test
  public void test_compute_objective_value() {
    final var remainingTime = maxTripDuration - tripDuration;
    assertEquals(remainingTime, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_compute_objective_value_delta_when_visiting_new_site() {
    assertEquals(
        -tripDurationDelta,
        objective.getVisitNewSiteObjectiveValueDelta(solution, site, tripDurationDelta).getValue());
  }

  @Test
  public void test_compute_objective_value_delta_when_unvisiting_site() {
    assertEquals(
        -tripDurationDelta,
        objective.getUnvisitSiteObjectiveValueDelta(solution, site, tripDurationDelta).getValue());
  }
}
