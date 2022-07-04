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

public class NumberOfVisitedCountriesObjectiveTest {

  private NumberOfVisitedCountriesObjective objective;
  private TravelStartLocation start;
  private Site afghanSite, algerianSite, afghanAndAlgerianSite, afghanAlgerianAndEnglishSite;
  TravelMatrix matrix;
  long tripDurationDelta;

  @BeforeEach
  public void setUp() {
    objective = new NumberOfVisitedCountriesObjective();
    afghanSite =
        Site.builder()
            .name("Cultural Landscape and Archaeological Remains of the Bamiyan Valley")
            .locationID(1)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Afghanistan"))))
            .type(SiteType.Cultural)
            .isEndangered()
            .build();
    algerianSite =
        Site.builder()
            .name("Tassili n'Ajjer")
            .locationID(2)
            .coordinates(new Coordinates(25.5, 9))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Algeria"))))
            .type(SiteType.Mixed)
            .build();
    afghanAndAlgerianSite =
        Site.builder()
            .name("Some site that exists for sure")
            .locationID(3)
            .coordinates(new Coordinates(-17.92453, 25.85539))
            .countries(
                new ArrayList<>(Arrays.asList(new Country("Afghanistan"), new Country("Algeria"))))
            .type(SiteType.Natural)
            .build();
    afghanAlgerianAndEnglishSite =
        Site.builder()
            .name("Some site that exists for sure as well")
            .locationID(4)
            .coordinates(new Coordinates(-17.92453, 25.85539))
            .countries(
                new ArrayList<>(
                    Arrays.asList(
                        new Country("Afghanistan"),
                        new Country("Algeria"),
                        new Country("England"))))
            .type(SiteType.Natural)
            .build();
    final var sites =
        new ArrayList<>(
            Arrays.asList(
                afghanSite, algerianSite, afghanAndAlgerianSite, afghanAlgerianAndEnglishSite));
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    matrix = new TravelMatrix(sites, start);
    tripDurationDelta = 1L;
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MAXIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_visited_countries_is_empty() {
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void
      test_get_objective_value_when_list_of_visited_countries_contains_same_number_of_sites_and_countries() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(afghanSite)
            .visitedSite(algerianSite)
            .build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void
      test_get_objective_value_when_list_of_visited_countries_contains_different_number_of_sites_and_countries() {
    final var solution =
        new SolutionBuilder().start(start).visitedSite(afghanAndAlgerianSite).build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void get_objectives_value_delta_when_adding_site_with_unvisited_country() {
    final var solution =
        new SolutionBuilder().start(start).unvisitedSite(afghanAndAlgerianSite).build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(2L).build();
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(
            solution, afghanAndAlgerianSite, tripDurationDelta));
  }

  @Test
  public void get_objectives_value_delta_when_adding_site_with_already_visited_country() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(afghanAndAlgerianSite)
            .unvisitedSite(afghanSite)
            .build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(0L).build();
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(solution, afghanSite, tripDurationDelta));
  }

  @Test
  public void
      get_objectives_value_delta_when_adding_site_with_some_visited_and_unvisited_countries() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(afghanAndAlgerianSite)
            .unvisitedSite(afghanAlgerianAndEnglishSite)
            .build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(1L).build();
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(
            solution, afghanAlgerianAndEnglishSite, tripDurationDelta));
  }

  @Test
  public void get_objectives_value_delta_when_removing_only_site_that_visits_two_countries() {
    final var solution =
        new SolutionBuilder().start(start).visitedSite(afghanAndAlgerianSite).build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(-2L).build();
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(
            solution, afghanAndAlgerianSite, tripDurationDelta));
  }

  @Test
  public void get_objectives_value_delta_when_removing_site_such_that_site_countries_are_still_visited() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(afghanAndAlgerianSite)
            .visitedSite(afghanSite)
            .build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(0L).build();
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(solution, afghanSite, tripDurationDelta));
  }

  @Test
  public void
  get_objectives_value_delta_when_removing_sites_with_some_countries_that_only_it_visits() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(afghanAndAlgerianSite)
            .visitedSite(afghanAlgerianAndEnglishSite)
            .build(matrix);
    final var expectedResult =
        ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(-1L).build();
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(
            solution, afghanAlgerianAndEnglishSite, tripDurationDelta));
  }
}
