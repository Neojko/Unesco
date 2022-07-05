package domain.constraints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.locations.sites.Sites;
import domain.solution.Solution;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LessThanXSiteTypeDifferenceConstraintTest {

  private Solution solution;
  private Sites visitedSites;
  private static Site culturalSite, naturalSite, mixedSite;
  private LessThanXSiteTypeDifferenceConstraint constraint;

  @BeforeEach
  public void setUp() {
    solution = mock(Solution.class);
    visitedSites = mock(Sites.class);
    when(solution.getVisitedSites()).thenReturn(visitedSites);
    culturalSite = Site.builder().type(SiteType.Cultural).build();
    naturalSite = Site.builder().type(SiteType.Natural).build();
    mixedSite = Site.builder().type(SiteType.Mixed).build();
  }

  private static Stream<Arguments> test_is_feasible() {
    return Stream.of(
        Arguments.of(0, 0, 0, true),
        Arguments.of(0, 0, 1, true),
        Arguments.of(1, 1, 0, true),
        Arguments.of(1, 1, 1, true),
        Arguments.of(0, 1, 0, false),
        Arguments.of(1, 0, 0, false),
        Arguments.of(5, 4, 0, false),
        Arguments.of(5, 4, 1, true),
        Arguments.of(4, 5, 0, false),
        Arguments.of(4, 5, 1, true),
        Arguments.of(5, 5, 0, true));
  }

  @ParameterizedTest
  @MethodSource
  public void test_is_feasible(
      final int cultural,
      final int natural,
      final int maxDifference,
      final boolean expectedResult) {
    when(visitedSites.getNumberOfCulturalSites()).thenReturn(cultural);
    when(visitedSites.getNumberOfNaturalSites()).thenReturn(natural);
    constraint = new LessThanXSiteTypeDifferenceConstraint(maxDifference);
    assertEquals(expectedResult, constraint.isFeasible(solution));
  }

  private static Stream<Arguments> test_can_visit_new_site() {
    return Stream.of(
        Arguments.of(0, 0, culturalSite, 0, false),
        Arguments.of(0, 0, naturalSite, 0, false),
        Arguments.of(0, 0, mixedSite, 0, true),
        Arguments.of(0, 0, culturalSite, 1, true),
        Arguments.of(0, 0, naturalSite, 1, true),
        Arguments.of(0, 0, mixedSite, 1, true),
        Arguments.of(2, 1, culturalSite, 0, false),
        Arguments.of(2, 1, naturalSite, 0, true),
        Arguments.of(2, 1, mixedSite, 0, false),
        Arguments.of(2, 1, culturalSite, 1, false),
        Arguments.of(2, 1, naturalSite, 1, true),
        Arguments.of(2, 1, mixedSite, 1, true),
        Arguments.of(1, 2, culturalSite, 0, true),
        Arguments.of(1, 2, naturalSite, 0, false),
        Arguments.of(1, 2, mixedSite, 0, false),
        Arguments.of(1, 2, culturalSite, 1, true),
        Arguments.of(1, 2, naturalSite, 1, false),
        Arguments.of(1, 2, mixedSite, 1, true));
  }

  @ParameterizedTest
  @MethodSource
  public void test_can_visit_new_site(
      final int cultural,
      final int natural,
      final Site site,
      final int maxDifference,
      final boolean expectedResult) {
    when(visitedSites.getNumberOfCulturalSites()).thenReturn(cultural);
    when(visitedSites.getNumberOfNaturalSites()).thenReturn(natural);
    constraint = new LessThanXSiteTypeDifferenceConstraint(maxDifference);
    assertEquals(expectedResult, constraint.canVisitNewSite(solution, site, 0, 0));
  }

  private static Stream<Arguments> test_can_unvisit_new_site() {
    return Stream.of(
        Arguments.of(0, 0, culturalSite, 0, false),
        Arguments.of(0, 0, naturalSite, 0, false),
        Arguments.of(0, 0, mixedSite, 0, true),
        Arguments.of(0, 0, culturalSite, 1, true),
        Arguments.of(0, 0, naturalSite, 1, true),
        Arguments.of(0, 0, mixedSite, 1, true),
        Arguments.of(2, 1, culturalSite, 0, true),
        Arguments.of(2, 1, naturalSite, 0, false),
        Arguments.of(2, 1, mixedSite, 0, false),
        Arguments.of(2, 1, culturalSite, 1, true),
        Arguments.of(2, 1, naturalSite, 1, false),
        Arguments.of(2, 1, mixedSite, 1, true),
        Arguments.of(1, 2, culturalSite, 0, false),
        Arguments.of(1, 2, naturalSite, 0, true),
        Arguments.of(1, 2, mixedSite, 0, false),
        Arguments.of(1, 2, culturalSite, 1, false),
        Arguments.of(1, 2, naturalSite, 1, true),
        Arguments.of(1, 2, mixedSite, 1, true));
  }

  @ParameterizedTest
  @MethodSource
  public void test_can_unvisit_new_site(
      final int cultural,
      final int natural,
      final Site site,
      final int maxDifference,
      final boolean expectedResult) {
    when(visitedSites.getNumberOfCulturalSites()).thenReturn(cultural);
    when(visitedSites.getNumberOfNaturalSites()).thenReturn(natural);
    constraint = new LessThanXSiteTypeDifferenceConstraint(maxDifference);
    assertEquals(expectedResult, constraint.canUnvisitSite(solution, site));
  }
}
