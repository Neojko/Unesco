package domain.objectives;

import static domain.locations.sites.SiteType.Cultural;
import static domain.locations.sites.SiteType.Mixed;
import static domain.locations.sites.SiteType.Natural;
import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.components.ObjectiveSense;
import domain.solution.Solution.SolutionBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SiteTypeParityObjectiveTest {

  private SiteTypeParityObjective objective;
  private TravelStartLocation start;
  private static Site natural1, natural2, cultural1, cultural2, mixed1, mixed2;
  TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    objective = new SiteTypeParityObjective();
    final var coordinates = new Coordinates(0, 0);
    final var country = new Country("Country");
    cultural1 =
        Site.builder()
            .name("cultural1")
            .locationID(1)
            .coordinates(coordinates)
            .country(country)
            .type(Cultural)
            .build();
    cultural2 =
        Site.builder()
            .name("cultural2")
            .locationID(2)
            .coordinates(coordinates)
            .country(country)
            .type(Cultural)
            .build();
    natural1 =
        Site.builder()
            .name("naturalSite1")
            .locationID(3)
            .coordinates(coordinates)
            .country(country)
            .type(Natural)
            .build();
    natural2 =
        Site.builder()
            .name("naturalSite2")
            .locationID(4)
            .coordinates(coordinates)
            .country(country)
            .type(Natural)
            .build();
    mixed1 =
        Site.builder()
            .name("mixedSite1")
            .locationID(5)
            .coordinates(coordinates)
            .country(country)
            .type(Mixed)
            .build();
    mixed2 =
        Site.builder()
            .name("mixedSite2")
            .locationID(6)
            .coordinates(coordinates)
            .country(country)
            .type(Mixed)
            .build();
    final var sites =
        new ArrayList<>(
            Arrays.asList(
                cultural1, cultural2, natural1, natural2, mixed1, mixed2));
    start = TravelStartLocation.builder().coordinates(coordinates).build();
    matrix = new TravelMatrix(sites, start);
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MINIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_visited_sites_is_empty() {
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  private static Stream<Arguments>
      test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site() {
    return Stream.of(
        Arguments.of(Collections.singletonList(cultural1), 1),
        Arguments.of(Collections.singletonList(natural1), 1),
        Arguments.of(Collections.singletonList(mixed1), 0),
        Arguments.of(Arrays.asList(cultural1, cultural2), 2),
        Arguments.of(Arrays.asList(cultural1, natural1), 0),
        Arguments.of(Arrays.asList(cultural1, mixed1), 1),
        Arguments.of(Arrays.asList(natural1, natural2), 2),
        Arguments.of(Arrays.asList(natural1, mixed1), 1),
        Arguments.of(Arrays.asList(mixed1, mixed2), 0));
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site(
      final List<Site> sites, final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var site : sites) {
      solutionBuilder = solutionBuilder.visitedSite(site);
    }
    final var solution = solutionBuilder.build(matrix);
    assertEquals(expectedResult, objective.computeObjectiveValue(solution).getValue());
  }

  private static Stream<Arguments> test_get_objective_value_delta_when_visiting_new_site() {
    return Stream.of(
        Arguments.of(Collections.singletonList(cultural1), cultural2, 1),
        Arguments.of(Collections.singletonList(cultural1), natural2, -1),
        Arguments.of(Collections.singletonList(cultural1), mixed2, 0),
        Arguments.of(Collections.singletonList(natural1), cultural2, -1),
        Arguments.of(Collections.singletonList(natural1), natural2, 1),
        Arguments.of(Collections.singletonList(natural1), mixed2, 0),
        Arguments.of(Collections.singletonList(mixed1), cultural2, 1),
        Arguments.of(Collections.singletonList(mixed1), natural2, 1),
        Arguments.of(Collections.singletonList(mixed1), mixed2, 0),
        Arguments.of(Arrays.asList(cultural1, natural1), cultural2, 1),
        Arguments.of(Arrays.asList(cultural1, mixed1), cultural2, 1),
        Arguments.of(Arrays.asList(natural1, mixed1), cultural2, -1),
        Arguments.of(Arrays.asList(mixed1, mixed2), cultural2, 1),
        Arguments.of(Arrays.asList(cultural1, natural1), natural2, 1),
        Arguments.of(Arrays.asList(cultural1, mixed1), natural2, -1),
        Arguments.of(Arrays.asList(natural1, mixed1), natural2, 1),
        Arguments.of(Arrays.asList(mixed1, mixed2), natural2, 1));
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_delta_when_visiting_new_site(
      final List<Site> sites, final Site siteToVisit, final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var site : sites) {
      solutionBuilder = solutionBuilder.visitedSite(site);
    }
    final var solution = solutionBuilder.build(matrix);
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(solution, siteToVisit, 0L).getValue());
  }

  private static Stream<Arguments> test_get_objective_value_delta_when_unvisiting_site() {
    return Stream.of(
        Arguments.of(Collections.singletonList(cultural1), cultural1, -1),
        Arguments.of(Collections.singletonList(natural1), natural1, -1),
        Arguments.of(Collections.singletonList(mixed1), mixed1, 0),
        Arguments.of(
            Arrays.asList(cultural1, cultural2), cultural1, -1),
        Arguments.of(
            Arrays.asList(cultural1, natural1), cultural1, 1),
        Arguments.of(Arrays.asList(cultural1, natural1), natural1, 1),
        Arguments.of(Arrays.asList(cultural1, mixed1), cultural1, -1),
        Arguments.of(Arrays.asList(cultural1, mixed1), mixed1, 0),
        Arguments.of(Arrays.asList(natural1, natural2), natural1, -1),
        Arguments.of(Arrays.asList(natural1, mixed1), natural1, -1),
        Arguments.of(Arrays.asList(natural1, mixed1), mixed1, 0),
        Arguments.of(Arrays.asList(mixed1, mixed2), mixed1, 0));
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_delta_when_unvisiting_site(
      final List<Site> sites, final Site siteToUnvisit, final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var site : sites) {
      solutionBuilder = solutionBuilder.visitedSite(site);
    }
    final var solution = solutionBuilder.build(matrix);
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(solution, siteToUnvisit, 0L).getValue());
  }
}
