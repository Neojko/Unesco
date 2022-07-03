package domain.constraints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.locations.sites.SiteReader;
import domain.locations.sites.SiteReaderTest;
import domain.matrix.TravelMatrix;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import java.util.List;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MaxTripDurationConstraintTest {

  private TravelStartLocation start;
  private Site site1, site2, site3;
  private TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    final List<Site> sites = new SiteReader().createSites(SiteReaderTest.testFile);
    site1 = sites.get(0);
    site2 = sites.get(1);
    site3 = sites.get(2);
    matrix = new TravelMatrix(sites, start);
  }

  private static Stream<Arguments> test_is_feasible() {
    return Stream.of(
        Arguments.of(-1L, false), // Max duration is 1s less than solution duration
        Arguments.of(0L, true), // Max duration is same as solution duration
        Arguments.of(1L, true) // Max duration is 1s more than solution duration
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_is_feasible(final long delta, final boolean expectedResult) {
    final var solution = Solution.builder().start(start).visitedSite(site1).build(matrix);
    final long maxDuration = solution.getTripDurationinSeconds() + delta;
    final var constraint = new MaxTripDurationConstraint(maxDuration);
    assertEquals(expectedResult, constraint.isFeasible(solution));
  }

  private static Stream<Arguments> test_can_visit_new_site_in_empty_solution() {
    return Stream.of(
        Arguments.of(-1L, false), // Max duration is 1s less than solution duration
        Arguments.of(0L, true), // Max duration is same as solution duration
        Arguments.of(1L, true) // Max duration is 1s more than solution duration
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_can_visit_new_site_in_empty_solution(
      final long delta, final boolean expectedResult) {
    final var solution = Solution.builder().start(start).unvisitedSite(site1).build(matrix);
    final var increase =
        matrix.time(start, site1)
            + SolutionTripDurationComputer.timePerSite
            + matrix.time(site1, start);
    final long maxDuration = solution.getTripDurationinSeconds() + increase + delta;
    final var constraint = new MaxTripDurationConstraint(maxDuration);
    final var result = constraint.canVisitNewSite(solution, site1, 0, increase);
    assertEquals(expectedResult, result);
  }

  private static Stream<Arguments> test_can_visit_new_site_when_solution_is_not_empty() {
    return Stream.of(
        Arguments.of(SitePosition.START, -1L, false), // 1s less than max duration
        Arguments.of(SitePosition.START, 0L, true), // Same duration as max duration
        Arguments.of(SitePosition.START, 1L, true), // 1s more than max duration
        Arguments.of(SitePosition.BETWEEN_TWO_SITES, -1L, false), // 1s less
        Arguments.of(SitePosition.BETWEEN_TWO_SITES, 0L, true), // Same
        Arguments.of(SitePosition.BETWEEN_TWO_SITES, 1L, true), // 1s more
        Arguments.of(SitePosition.END, -1L, false), // 1s less than max duration
        Arguments.of(SitePosition.END, 0L, true), // same as max duration
        Arguments.of(SitePosition.END, 1L, true) // 1s more than max duration
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_can_visit_new_site_when_solution_is_not_empty(
      final SitePosition position, final long delta, final boolean expectedResult) {
    final var solution =
        Solution.builder()
            .start(start)
            .visitedSite(site1)
            .visitedSite(site2)
            .unvisitedSite(site3)
            .build(matrix);
    final var increase =
        SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
            solution, site3, getPosition(position), matrix);
    final long maxDuration = solution.getTripDurationinSeconds() + increase + delta;
    final var constraint = new MaxTripDurationConstraint(maxDuration);
    final var result = constraint.canVisitNewSite(solution, site3, getPosition(position), increase);
    assertEquals(expectedResult, result);
  }

  private int getPosition(final SitePosition sitePosition) {
    switch (sitePosition) {
      case START:
        return 0;
      case BETWEEN_TWO_SITES:
        return 1;
      default: // END
        return 2;
    }
  }

  private enum SitePosition {
    START,
    BETWEEN_TWO_SITES,
    END
  }
}
