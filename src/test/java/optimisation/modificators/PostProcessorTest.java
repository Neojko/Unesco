package optimisation.modificators;

import static domain.locations.sites.SiteType.Cultural;
import static domain.locations.sites.SiteType.Mixed;
import static domain.locations.sites.SiteType.Natural;
import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.constraints.ConstraintManager;
import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution.SolutionBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PostProcessorTest {

  private TravelStartLocation start;
  private Site natural1, natural2, cultural1, cultural2, mixed1, mixed2;
  private TravelMatrix matrix;
  private ConstraintManager constraintManager;
  private ObjectiveManager objectiveManager;

  @BeforeEach
  public void setUp() {
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
        new ArrayList<>(Arrays.asList(cultural1, cultural2, natural1, natural2, mixed1, mixed2));
    start = TravelStartLocation.builder().coordinates(coordinates).build();
    matrix = new TravelMatrix(sites, start);
    constraintManager = ConstraintManager.builder().build();
    objectiveManager = ObjectiveManager.builder().build();
  }

  private static Stream<Arguments> test_fix() {
    return Stream.of(
        // Zero site
        Arguments.of(new ArrayList<>(), 0),

        // One site
        Arguments.of(Collections.singletonList(SiteName.CULTURAL_1), 0),
        Arguments.of(Collections.singletonList(SiteName.NATURAL_1), 0),
        Arguments.of(Collections.singletonList(SiteName.MIXED_1), 1),

        // Two sites
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.CULTURAL_2), 0),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1), 2),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.MIXED_1), 1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.NATURAL_2), 0),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1), 1),
        Arguments.of(Arrays.asList(SiteName.MIXED_1, SiteName.MIXED_2), 2),

        // Three sites
        Arguments.of(
            Arrays.asList(SiteName.CULTURAL_1, SiteName.CULTURAL_2, SiteName.NATURAL_1), 2),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.CULTURAL_2, SiteName.MIXED_1), 1),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1, SiteName.NATURAL_2), 2),
        Arguments.of(Arrays.asList(SiteName.CULTURAL_1, SiteName.NATURAL_1, SiteName.MIXED_1), 3),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.NATURAL_2, SiteName.MIXED_1), 1),
        Arguments.of(Arrays.asList(SiteName.NATURAL_1, SiteName.MIXED_1, SiteName.MIXED_2), 2),

        // More sites
        Arguments.of(
            Arrays.asList(
                SiteName.CULTURAL_1, SiteName.CULTURAL_2, SiteName.NATURAL_1, SiteName.NATURAL_2),
            4),
        Arguments.of(
            Arrays.asList(
                SiteName.CULTURAL_1,
                SiteName.CULTURAL_2,
                SiteName.NATURAL_1,
                SiteName.MIXED_1,
                SiteName.MIXED_2),
            4),
        Arguments.of(
            Arrays.asList(
                SiteName.CULTURAL_1,
                SiteName.NATURAL_1,
                SiteName.NATURAL_2,
                SiteName.MIXED_1,
                SiteName.MIXED_2),
            4),
        Arguments.of(
            Arrays.asList(
                SiteName.CULTURAL_1,
                SiteName.CULTURAL_2,
                SiteName.NATURAL_1,
                SiteName.NATURAL_2,
                SiteName.MIXED_1,
                SiteName.MIXED_2),
            6));
  }

  @ParameterizedTest
  @MethodSource
  public void test_fix(final List<SiteName> siteNames, final int expectedNoOfVisitedSitesAfterFix) {
    var solutionBuilder = new SolutionBuilder().start(start);
    for (final var siteName : siteNames) {
      solutionBuilder = solutionBuilder.visitedSite(getSite(siteName));
    }
    final var solution = solutionBuilder.build(constraintManager, objectiveManager, matrix);
    PostProcesser.fix(constraintManager, objectiveManager, matrix, solution);
    final var cultural = solution.getVisitedSites().getNumberOfCulturalSites();
    final var natural = solution.getVisitedSites().getNumberOfNaturalSites();
    assertEquals(cultural, natural);
    assertEquals(expectedNoOfVisitedSitesAfterFix, solution.getVisitedSites().getSites().size());
  }

  private Site getSite(final SiteName siteName) {
    switch (siteName) {
      case CULTURAL_1:
        return cultural1;
      case CULTURAL_2:
        return cultural2;
      case NATURAL_1:
        return natural1;
      case NATURAL_2:
        return natural2;
      case MIXED_1:
        return mixed1;
      case MIXED_2:
        return mixed2;
      default:
        throw new IllegalArgumentException();
    }
  }

  private enum SiteName {
    CULTURAL_1,
    CULTURAL_2,
    NATURAL_1,
    NATURAL_2,
    MIXED_1,
    MIXED_2
  }
}
