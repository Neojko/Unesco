package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import domain.solution.Solution.SolutionBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberOfVisitedEndangeredSitesObjectiveTest {

  private NumberOfVisitedEndangeredSitesObjective objective;
  private Site endangeredSite, notEndangeredSite;
  private TravelStartLocation start;
  TravelMatrix matrix;
  long tripDurationDelta;

  @BeforeEach
  public void setUp() {
    objective = new NumberOfVisitedEndangeredSitesObjective();
    endangeredSite =
        Site.builder()
            .name("Endangered site")
            .locationID(1)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Afghanistan"))))
            .type(SiteType.Cultural)
            .isEndangered()
            .build();
    notEndangeredSite =
        Site.builder()
            .name("Not endangered site")
            .locationID(2)
            .coordinates(new Coordinates(25.5, 9))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Algeria"))))
            .type(SiteType.Mixed)
            .build();
    final var sites = new ArrayList<>(Arrays.asList(endangeredSite, notEndangeredSite));
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    matrix = new TravelMatrix(sites, start);
    tripDurationDelta = 1L;
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MAXIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_endangered_visited_sites_is_empty() {
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_value_when_visited_sites_are_all_endangered() {
    final var solution =
        new SolutionBuilder().start(start).visitedSite(endangeredSite).build(matrix);
    assertEquals(1L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_value_when_visited_sites_are_not_all_endangered() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(endangeredSite)
            .visitedSite(notEndangeredSite)
            .build(matrix);
    assertEquals(1L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_values_delta_when_visiting_new_endangered_site() {
    final var solution =
        new SolutionBuilder().start(start).unvisitedSite(endangeredSite).build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(1L).build();
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(solution, endangeredSite, tripDurationDelta));
  }

  @Test
  public void test_get_objective_values_delta_when_visiting_new_not_endangered_site() {
    final var solution =
        new SolutionBuilder().start(start).unvisitedSite(notEndangeredSite).build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(0L).build();
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(
            solution, notEndangeredSite, tripDurationDelta));
  }

  @Test
  public void test_get_objective_values_delta_when_unvisiting_endangered_site() {
    final var solution =
        new SolutionBuilder().start(start).visitedSite(endangeredSite).build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(-1L).build();
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(solution, endangeredSite, tripDurationDelta));
  }

  @Test
  public void test_get_objective_values_delta_when_unvisiting_not_endangered_site() {
    final var solution =
        new SolutionBuilder().start(start).visitedSite(notEndangeredSite).build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(0L).build();
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(
            solution, notEndangeredSite, tripDurationDelta));
  }
}
