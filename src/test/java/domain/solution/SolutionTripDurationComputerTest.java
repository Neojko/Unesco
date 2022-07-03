package domain.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.locations.sites.SiteReader;
import domain.locations.sites.SiteReaderTest;
import domain.matrix.TravelMatrix;
import java.util.List;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SolutionTripDurationComputerTest {

  private TravelStartLocation start;
  private Site site1, site2, site3;
  private TravelMatrix matrix;
  private Solution solution;

  @BeforeEach
  public void setUp() {
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    final List<Site> sites = new SiteReader().createSites(SiteReaderTest.testFile);
    site1 = sites.get(0);
    site2 = sites.get(1);
    site3 = sites.get(2);
    matrix = new TravelMatrix(sites, start);
    solution =
        Solution.builder()
            .start(start)
            .visitedSite(site1)
            .visitedSite(site2)
            .unvisitedSite(site3)
            .build(matrix);
  }

  @Test
  public void test_compute_solution_trip_duration() {
    final var expectedResult =
        SolutionTripDurationComputer.timePerSite * 2
            + matrix.time(start, site1)
            + matrix.time(site1, site2)
            + matrix.time(site2, start);
    assertEquals(
        expectedResult, SolutionTripDurationComputer.computeTripDuration(solution, matrix));
  }

  private static Stream<Arguments> test_can_visit_new_site_when_solution_is_not_empty() {
    return Stream.of(
        Arguments.of(SitePosition.START),
        Arguments.of(SitePosition.BETWEEN_TWO_SITES),
        Arguments.of(SitePosition.END));
  }

  @ParameterizedTest
  @MethodSource
  public void test_can_visit_new_site_when_solution_is_not_empty(final SitePosition sitePosition) {
    final long a = getTripDurationDelta(sitePosition);

    assertEquals(
        getTripDurationDelta(sitePosition),
        SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
            solution, site3, getPosition(sitePosition), matrix));
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

  private long getTripDurationDelta(final SitePosition sitePosition) {
    switch (sitePosition) {
      case START:
        return matrix.time(start, site3)
            + SolutionTripDurationComputer.timePerSite
            + matrix.time(site3, site1)
            - matrix.time(start, site1);
      case BETWEEN_TWO_SITES:
        return matrix.time(site1, site3)
            + SolutionTripDurationComputer.timePerSite
            + matrix.time(site3, site2)
            - matrix.time(site1, site2);
      default: // END
        return matrix.time(site2, site3)
            + SolutionTripDurationComputer.timePerSite
            + matrix.time(site3, start)
            - matrix.time(site2, start);
    }
  }

  private enum SitePosition {
    START,
    BETWEEN_TWO_SITES,
    END
  }
}