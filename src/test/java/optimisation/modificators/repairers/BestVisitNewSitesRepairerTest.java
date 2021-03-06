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
import java.util.stream.Stream;
import lombok.var;
import optimisation.choosers.filters.AcceptAllFilter;
import optimisation.criteria.stopping.NumberOfIterationsStoppingCriterion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BestVisitNewSitesRepairerTest {

  private TravelStartLocation start;
  private static Site interestingSite, lessInterestingSite;
  private TravelMatrix matrix;
  private Solution solution;
  private ConstraintManager constraintManager;
  private ObjectiveManager objectiveManager;

  @BeforeEach
  public void setUp() {
    final var country = new Country("France");
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    interestingSite =
        Site.builder()
            .locationID(1)
            .name("interestingSite")
            .country(country)
            .coordinates(new Coordinates(0, 1))
            .type(SiteType.Natural)
            .isEndangered()
            .build();
    lessInterestingSite =
        Site.builder()
            .locationID(2)
            .name("lessInterestingSite")
            .country(country)
            .coordinates(new Coordinates(0, 10))
            .type(SiteType.Natural)
            .build();
    final var locations = Arrays.asList(start, interestingSite, lessInterestingSite);
    matrix = new TravelMatrix(locations);
    constraintManager = ConstraintManager.builder().build();
    objectiveManager =
        ObjectiveManager.builder().objective(new NumberOfVisitedEndangeredSitesObjective()).build();
    solution =
        Solution.builder()
            .start(start)
            .unvisitedSite(interestingSite)
            .unvisitedSite(lessInterestingSite)
            .build(constraintManager, objectiveManager, matrix);
  }

  @Test
  public void test_repair_on_empty_solution() {
    final var emptySolution =
        Solution.builder().start(start).build(constraintManager, objectiveManager, matrix);
    final var repairer =
        BestVisitNewSitesRepairer.builder()
            .filter(new AcceptAllFilter())
            .stoppingCriterion(new NumberOfIterationsStoppingCriterion(1))
            .build();
    repairer.repair(constraintManager, objectiveManager, matrix, emptySolution);
    assertTrue(emptySolution.getVisitedSites().getSites().isEmpty());
  }

  private static Stream<Arguments> test_repair() {
    return Stream.of(
        Arguments.of(1, Collections.singletonList(interestingSite), true),
        Arguments.of(2, Arrays.asList(lessInterestingSite, interestingSite), true),
        Arguments.of(3, Arrays.asList(lessInterestingSite, interestingSite), false));
  }

  @ParameterizedTest
  @MethodSource
  public void test_repair(
      final int maxIterations,
      final List<Site> expectedVisitedSites,
      final boolean isStoppingCriterionMet) {
    final var stoppingCriterion = new NumberOfIterationsStoppingCriterion(maxIterations);
    final var repairer =
        BestVisitNewSitesRepairer.builder()
            .filter(new AcceptAllFilter())
            .stoppingCriterion(stoppingCriterion)
            .build();
    repairer.repair(constraintManager, objectiveManager, matrix, solution);
    assertEquals(expectedVisitedSites, solution.getVisitedSites().getSites());
    assertEquals(isStoppingCriterionMet, stoppingCriterion.isMet());
  }
}
