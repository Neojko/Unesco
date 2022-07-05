package optimisation.modificators.destroyers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.constraints.ConstraintManager;
import domain.constraints.LessThanXSiteTypeDifferenceConstraint;
import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import java.util.Arrays;
import java.util.stream.Stream;
import lombok.var;
import optimisation.choosers.filters.AcceptAllFilter;
import optimisation.choosers.filters.SiteFilter;
import optimisation.choosers.selectors.SiteSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RandomSiteDestroyerTest {

  private Site culturalSite, naturalSite;
  private TravelStartLocation start;
  private TravelMatrix matrix;
  private Solution solution;
  private ConstraintManager constraintManager;
  private ObjectiveManager objectiveManager;
  private SiteFilter filter;
  private SiteSelector selector;

  @BeforeEach
  public void setUp() {
    final var country = new Country("France");
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    culturalSite =
        Site.builder()
            .locationID(1)
            .name("site1")
            .country(country)
            .coordinates(new Coordinates(0, 1))
            .type(SiteType.Natural)
            .isEndangered()
            .build();
    naturalSite =
        Site.builder()
            .locationID(2)
            .name("site2")
            .country(country)
            .coordinates(new Coordinates(0, 10))
            .type(SiteType.Cultural)
            .build();
    final var locations = Arrays.asList(start, culturalSite, naturalSite);
    matrix = new TravelMatrix(locations);
    constraintManager = ConstraintManager.builder().build();
    objectiveManager = ObjectiveManager.builder().build();
    solution =
        Solution.builder()
            .start(start)
            .visitedSite(culturalSite)
            .visitedSite(naturalSite)
            .build(matrix);
    filter = new AcceptAllFilter();
    selector = mock(SiteSelector.class);
  }

  private static Stream<Arguments> test_destroy() {
    return Stream.of(
        Arguments.of(0),
        Arguments.of(0.25),
        Arguments.of(0.25001),
        Arguments.of(0.50),
        Arguments.of(.74999),
        Arguments.of(.75));
  }

  @ParameterizedTest
  @MethodSource
  public void test_destroy(final double percentage) {
    final var randomDestroyer =
        RandomSiteDestroyer.builder()
            .percentage(percentage)
            .selector(selector)
            .filter(filter)
            .build();
    when(selector.select(any())).thenReturn(culturalSite).thenReturn(naturalSite);
    randomDestroyer.destroy(constraintManager, objectiveManager, matrix, solution);
    final var expectedSolution = getExpectedSolution(percentage);
    assertEquals(expectedSolution, solution);
  }

  private Solution getExpectedSolution(final double percentage) {
    if (percentage < 0.25) {
      return solution.copy();
    }
    if (percentage < 0.75) {
      return Solution.builder()
          .start(start)
          .visitedSite(naturalSite)
          .unvisitedSite(culturalSite)
          .build(matrix);
    }
    return Solution.builder()
        .start(start)
        .unvisitedSite(culturalSite)
        .unvisitedSite(naturalSite)
        .build(matrix);
  }

  @Test
  public void test_destroy_with_not_empty_constraint_manager() {
    when(selector.select(any())).thenReturn(culturalSite);
    final var randomDestroyer =
        RandomSiteDestroyer.builder().percentage(1).selector(selector).filter(filter).build();
    final var constraint = LessThanXSiteTypeDifferenceConstraint.builder().maxDifference(0).build();
    constraintManager = ConstraintManager.builder().constraint(constraint).build();
    randomDestroyer.destroy(constraintManager, objectiveManager, matrix, solution);
    // Destroyer couldn't unvisit naturalSite without breaking the constraint even with percentage 1
    assertTrue(solution.getVisitedSites().containsSite(naturalSite));
  }
}
