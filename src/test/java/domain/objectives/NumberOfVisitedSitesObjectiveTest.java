package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Solution.SolutionBuilder;
import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberOfVisitedSitesObjectiveTest {

  private NumberOfVisitedSitesObjective objective;
  private Site site1, site2;
  private TravelStartLocation start;
  TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    objective = new NumberOfVisitedSitesObjective();
    site1 =
        Site.builder()
            .name("site1")
            .locationID(1)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Afghanistan"))))
            .type(SiteType.Cultural)
            .isEndangered()
            .build();
    site2 =
        Site.builder()
            .name("site2")
            .locationID(2)
            .coordinates(new Coordinates(25.5, 9))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Algeria"))))
            .type(SiteType.Mixed)
            .build();
    final var sites = new ArrayList<>(Arrays.asList(site1, site2));
    start = TravelStartLocation.builder().coordinates(0, 0).build();
    matrix = new TravelMatrix(sites, start);
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MAXIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_visited_sites_is_empty() {
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_value_when_list_of_visited_sites_contains_at_least_one_site() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(site1).visitedSite(site2)
            .build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_values_delta_when_visiting_new_site() {
    final var solution =
        new SolutionBuilder()
            .start(start)
            .unvisitedSite(site1)
            .build(matrix);
    final var expectedResult = ObjectiveValue.builder()
        .sense(ObjectiveSense.MAXIMIZE)
        .value(1L)
        .build();
    assertEquals(
        expectedResult,
        objective.getVisitNewSiteObjectiveValueDelta(solution, site1)
    );
  }
}
