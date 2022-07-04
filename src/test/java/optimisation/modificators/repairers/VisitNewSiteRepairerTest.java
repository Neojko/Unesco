package optimisation.modificators.repairers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.constraints.ConstraintManager;
import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.objectives.NumberOfVisitedEndangeredSitesObjective;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.var;
import optimisation.choosers.filters.AcceptAllFilter;
import optimisation.criteria.stopping.NumberOfIterationsStoppingCriterion;
import optimisation.criteria.stopping.StoppingCriterion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class VisitNewSiteRepairerTest {

  private TravelStartLocation start;
  private Site interestingSite, lessInterestingSite;
  private TravelMatrix matrix;
  private Solution solution;
  private ConstraintManager constraintManager;
  private ObjectiveManager objectiveManager;
  private StoppingCriterion stoppingCriterion;
  private VisitNewSitesRepairer repairer;

  @BeforeEach
  public void setUp() {
    final var country = new Country("France");
    start = TravelStartLocation.builder().coordinates(0,0).build();
    interestingSite = Site.builder()
        .locationID(1)
        .name("closestSite")
        .country(country)
        .coordinates(new Coordinates(0, 1))
        .type(SiteType.Natural)
        .isEndangered()
        .build();
    lessInterestingSite = Site.builder()
        .locationID(2)
        .name("farthestSite")
        .country(country)
        .coordinates(new Coordinates(0, 10))
        .type(SiteType.Natural)
        .build();
    final var locations = Arrays.asList(start, interestingSite, lessInterestingSite);
    matrix = new TravelMatrix(locations);
    final var solutionBuilder = Solution.builder()
        .start(start)
        .unvisitedSite(interestingSite).unvisitedSite(lessInterestingSite)
        .objective(new NumberOfVisitedEndangeredSitesObjective());
    constraintManager = solutionBuilder.getConstraintManager();
    objectiveManager = solutionBuilder.getObjectiveManager();
    solution = solutionBuilder.build(matrix);
  }

  @Test
  public void test_repair_on_empty_solution() {
    final var emptySolution = Solution.builder()
        .start(start)
        .objective(new NumberOfVisitedEndangeredSitesObjective())
        .build(matrix);
    final var repairer = VisitNewSitesRepairer.builder()
        .filter(new AcceptAllFilter())
        .stoppingCriterion(new NumberOfIterationsStoppingCriterion(1))
        .build();
    repairer.repair(constraintManager, objectiveManager, matrix, emptySolution);
    assertTrue(emptySolution.getVisitedSites().getSites().isEmpty());
  }

  private static Stream<Arguments> test_repair() {
    return Stream.of(
        Arguments.of(1, Collections.singletonList(SiteName.INTERESTING), true),
        Arguments.of(2, Arrays.asList(SiteName.INTERESTING, SiteName.LESS_INTERESTING), true),
        Arguments.of(3, Arrays.asList(SiteName.INTERESTING, SiteName.LESS_INTERESTING), false)
    );
  }

  @ParameterizedTest
  @MethodSource
  public void test_repair(
      final int maxIterations,
      final List<SiteName> expectedVisitedSiteNames,
      final boolean isStoppingCriterionMet
  ) {
    final var stoppingCriterion = new NumberOfIterationsStoppingCriterion(maxIterations);
    final var repairer = VisitNewSitesRepairer.builder()
        .filter(new AcceptAllFilter())
        .stoppingCriterion(stoppingCriterion)
        .build();
    repairer.repair(constraintManager, objectiveManager, matrix, solution);
    final var expectedVisitedSites = expectedVisitedSiteNames.stream()
        .map(this::getSite)
        .collect(Collectors.toList());
    assertEquals(expectedVisitedSites, solution.getVisitedSites().getSites());
    assertEquals(isStoppingCriterionMet, stoppingCriterion.isMet());
  }

  private Site getSite(final SiteName name) {
    if (name == SiteName.INTERESTING) {
      return interestingSite;
    }
    return lessInterestingSite;
  }

  private enum SiteName {
    INTERESTING,
    LESS_INTERESTING
  }

}
