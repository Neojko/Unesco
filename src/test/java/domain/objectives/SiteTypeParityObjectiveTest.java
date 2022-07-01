package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Solution.SolutionBuilder;
import domain.matrix.TravelMatrix;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.interfaces.Objective;
import domain.site.Coordinates;
import domain.site.SiteReader;
import domain.site.SiteReaderTest;
import java.util.ArrayList;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SiteTypeParityObjectiveTest {

  private Objective objective;

  @BeforeEach
  public void setUp() {
    objective = new SiteTypeParityObjective();
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MINIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_visited_sites_is_empty() {
    final var matrix = new TravelMatrix(new ArrayList<>());
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  private static Stream<Arguments>
      test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site() {
    return Stream.of(
        Arguments.of(0, 1), // 1 cultural and 0 natural
        Arguments.of(2, 1), // 0 cultural and 1 natural
        Arguments.of(1, 0) // 1 mixed (so no difference)
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site(
      final int siteIndexToVisit, final int expectedResult) {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var matrix = new TravelMatrix(sites);
    final var solution =
        new SolutionBuilder()
            .start(new Coordinates(0, 0))
            .visitedSite(sites.get(siteIndexToVisit))
            .build(matrix);
    assertEquals(expectedResult, objective.computeObjectiveValue(solution).getValue());
  }
}
