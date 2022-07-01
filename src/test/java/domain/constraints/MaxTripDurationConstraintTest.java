package domain.constraints;

import domain.Solution;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.locations.sites.SiteReader;
import domain.locations.sites.SiteReaderTest;
import domain.matrix.TravelMatrix;
import java.util.List;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MaxTripDurationConstraintTest {

  private TravelStartLocation start;
  private List<Site> sites;
  private Site site1, site2, site3;
  private TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    sites = new SiteReader().createSites(SiteReaderTest.testFile);
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
  public void test_is_feasible(
      final long delta,
      final boolean expectedResult
  ) {
    final var solution = Solution.builder().start(start).visitedSite(site1).build(matrix);
    final long maxDuration = solution.getDurationInSeconds() + delta;
    final var constraint = new MaxTripDurationConstraint(maxDuration);
    Assertions.assertEquals(expectedResult, constraint.isFeasible(solution));
  }

  @Test
  public void test_can_visit_new_site_in_empty_solution() {
    final var solution = Solution.builder().start(start).visitedSite(site1).build(matrix);
    final long maxDuration = solution.getDurationInSeconds() + ;
    final var constraint = new MaxTripDurationConstraint(maxDuration);
    Assertions.assertEquals(expectedResult, constraint.isFeasible(solution));
  }

}
