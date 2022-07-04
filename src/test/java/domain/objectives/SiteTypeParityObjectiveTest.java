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
  private Site naturalSite1, naturalSite2, culturalSite1, culturalSite2, mixedSite1, mixedSite2;
  TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    objective = new SiteTypeParityObjective();
    final var coordinates = new Coordinates(0, 0);
    final var country = new Country("Country");
    culturalSite1 =
        Site.builder()
            .name("cultural1")
            .locationID(1)
            .coordinates(coordinates)
            .country(country)
            .type(Cultural)
            .build();
    culturalSite2 =
        Site.builder()
            .name("cultural2")
            .locationID(2)
            .coordinates(coordinates)
            .country(country)
            .type(Cultural)
            .build();
    naturalSite1 =
        Site.builder()
            .name("naturalSite1")
            .locationID(3)
            .coordinates(coordinates)
            .country(country)
            .type(Natural)
            .build();
    naturalSite2 =
        Site.builder()
            .name("naturalSite2")
            .locationID(4)
            .coordinates(coordinates)
            .country(country)
            .type(Natural)
            .build();
    mixedSite1 =
        Site.builder()
            .name("mixedSite1")
            .locationID(5)
            .coordinates(coordinates)
            .country(country)
            .type(Mixed)
            .build();
    mixedSite2 =
        Site.builder()
            .name("mixedSite2")
            .locationID(6)
            .coordinates(coordinates)
            .country(country)
            .type(Mixed)
            .build();
    final var sites = new ArrayList<>(Arrays.asList
        (culturalSite1, culturalSite2, naturalSite1, naturalSite2, mixedSite1, mixedSite2));
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
        Arguments.of(Collections.singletonList(SiteName.CULTURAL_1), 1),
        Arguments.of(Collections.singletonList(SiteName.NATURAL_1), 1),
        Arguments.of(Collections.singletonList(SiteName.MIXED_1), 0),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.CULTURAL_2), 2),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1), 0),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.MIXED_1), 1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.NATURAL_2), 2),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1), 1),
        Arguments.of(Arrays.asList(SiteName.MIXED_1, SiteName.MIXED_2), 0)
    );
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site(
      final List<SiteName> names, final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var name : names) {
      solutionBuilder = solutionBuilder.visitedSite(getSite(name));
    }
    final var solution = solutionBuilder.build(matrix);
    assertEquals(expectedResult, objective.computeObjectiveValue(solution).getValue());
  }

  private static Stream<Arguments>
  test_get_objective_value_delta_when_visiting_new_site() {
    return Stream.of(
        Arguments.of(Collections.singletonList(SiteName.CULTURAL_1), SiteName.CULTURAL_2, 1),
        Arguments.of(Collections.singletonList(SiteName.CULTURAL_1), SiteName.NATURAL_2, -1),
        Arguments.of(Collections.singletonList(SiteName.CULTURAL_1), SiteName.MIXED_2, 0),
        Arguments.of(Collections.singletonList(SiteName.NATURAL_1), SiteName.CULTURAL_2, -1),
        Arguments.of(Collections.singletonList(SiteName.NATURAL_1), SiteName.NATURAL_2, 1),
        Arguments.of(Collections.singletonList(SiteName.NATURAL_1), SiteName.MIXED_2, 0),
        Arguments.of(Collections.singletonList(SiteName.MIXED_1), SiteName.CULTURAL_2, 1),
        Arguments.of(Collections.singletonList(SiteName.MIXED_1), SiteName.NATURAL_2, 1),
        Arguments.of(Collections.singletonList(SiteName.MIXED_1), SiteName.MIXED_2, 0),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1), SiteName.CULTURAL_2, 1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.MIXED_1), SiteName.CULTURAL_2, 1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1), SiteName.CULTURAL_2, -1),
        Arguments.of(Arrays.asList(SiteName.MIXED_1, SiteName.MIXED_2), SiteName.CULTURAL_2, 1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1), SiteName.NATURAL_2, 1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.MIXED_1), SiteName.NATURAL_2, -1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1), SiteName.NATURAL_2, 1),
        Arguments.of(Arrays.asList(SiteName.MIXED_1, SiteName.MIXED_2), SiteName.NATURAL_2, 1)
    );
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_delta_when_visiting_new_site(
      final List<SiteName> names,
      final SiteName siteNameToVisit,
      final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var name : names) {
      solutionBuilder = solutionBuilder.visitedSite(getSite(name));
    }
    final var siteToVisit = getSite(siteNameToVisit);
    final var solution = solutionBuilder.build(matrix);
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(solution, siteToVisit, 0L).getValue());
  }

  private static Stream<Arguments>
  test_get_objective_value_delta_when_unvisiting_site() {
    return Stream.of(
        Arguments.of(Collections.singletonList(SiteName.CULTURAL_1), SiteName.CULTURAL_1, -1),
        Arguments.of(Collections.singletonList(SiteName.NATURAL_1), SiteName.NATURAL_1, -1),
        Arguments.of(Collections.singletonList(SiteName.MIXED_1), SiteName.MIXED_1, 0),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.CULTURAL_2), SiteName.CULTURAL_1, -1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1), SiteName.CULTURAL_1, 1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1), SiteName.NATURAL_1, 1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.MIXED_1), SiteName.CULTURAL_1, -1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.MIXED_1), SiteName.MIXED_1, 0),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.NATURAL_2), SiteName.NATURAL_1, -1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1), SiteName.NATURAL_1, -1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1), SiteName.MIXED_1, 0),
        Arguments.of(Arrays.asList(SiteName.MIXED_1, SiteName.MIXED_2), SiteName.MIXED_1, 0)
    );
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_delta_when_unvisiting_site(
      final List<SiteName> names,
      final SiteName siteNameToVisit,
      final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var name : names) {
      solutionBuilder = solutionBuilder.visitedSite(getSite(name));
    }
    final var siteToUnvisit = getSite(siteNameToVisit);
    final var solution = solutionBuilder.build(matrix);
    assertEquals(
        expectedResult,
        objective.getUnvisitSiteObjectiveValueDelta(solution, siteToUnvisit, 0L).getValue());
  }

  private Site getSite(final SiteName siteName) {
    switch (siteName) {
      case CULTURAL_1:
        return culturalSite1;
      case CULTURAL_2:
        return culturalSite2;
      case NATURAL_1:
        return naturalSite1;
      case NATURAL_2:
        return naturalSite2;
      case MIXED_1:
        return mixedSite1;
      default:
        return mixedSite2;
    }
  }

  enum SiteName {
    CULTURAL_1,
    CULTURAL_2,
    NATURAL_1,
    NATURAL_2,
    MIXED_1,
    MIXED_2
  }
}
