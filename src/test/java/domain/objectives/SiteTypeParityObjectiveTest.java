package domain.objectives;

import static domain.locations.sites.SiteType.Cultural;
import static domain.locations.sites.SiteType.Mixed;
import static domain.locations.sites.SiteType.Natural;
import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
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
  private Site naturalSite, culturalSite, mixedSite;
  TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    objective = new SiteTypeParityObjective();
    culturalSite =
        Site.builder()
            .name("Cultural Landscape and Archaeological Remains of the Bamiyan Valley")
            .locationID(1)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Afghanistan"))))
            .type(SiteType.Cultural)
            .isEndangered()
            .build();
    mixedSite =
        Site.builder()
            .name("Tassili n'Ajjer")
            .locationID(2)
            .coordinates(new Coordinates(25.5, 9))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Algeria"))))
            .type(SiteType.Mixed)
            .build();
    naturalSite =
        Site.builder()
            .name("Some site that exists for sure")
            .locationID(3)
            .coordinates(new Coordinates(-17.92453, 25.85539))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Algeria"))))
            .type(Natural)
            .build();
    final var sites = new ArrayList<>(Arrays.asList(culturalSite, naturalSite, mixedSite));
    start = TravelStartLocation.builder().coordinates(0, 0).build();
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
        Arguments.of(Collections.singletonList(Natural), 1), // 1 cultural and 0 natural
        Arguments.of(Collections.singletonList(Cultural), 1), // 0 cultural and 1 natural
        Arguments.of(Collections.singletonList(Mixed), 0), // 1 cultural and 1 natural
        Arguments.of(Arrays.asList(Natural, Cultural), 0), // 1 cultural and 1 natural
        Arguments.of(Arrays.asList(Natural, Mixed), 1), // 1 cultural and 2 natural
        Arguments.of(Arrays.asList(Cultural, Mixed), 1), // 2 cultural and 1 natural
        Arguments.of(Arrays.asList(Natural, Natural), 2), // 0 cultural and 2 natural
        Arguments.of(Arrays.asList(Cultural, Cultural), 2), // 2 cultural and 0 natural
        Arguments.of(Arrays.asList(Mixed, Mixed), 0) // 2 cultural and 2 natural
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site(
      final List<SiteType> types, final int expectedResult) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var type : types) {
      solutionBuilder = solutionBuilder.visitedSite(getSite(type));
    }
    final var solution = solutionBuilder.build(matrix);
    assertEquals(expectedResult, objective.computeObjectiveValue(solution).getValue());
  }

  private Site getSite(final SiteType siteType) {
    switch (siteType) {
      case Natural:
        return naturalSite;
      case Cultural:
        return culturalSite;
      default:
        return mixedSite;
    }
  }
}
