package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Solution.SolutionBuilder;
import domain.locations.TravelStartLocation;
import domain.locations.sites.SiteReader;
import domain.locations.sites.SiteReaderTest;
import domain.matrix.TravelMatrix;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.interfaces.Objective;
import java.util.ArrayList;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NumberOfVisitedEndangeredSitesObjectiveTest {

  private Objective objective;

  @BeforeEach
  public void setUp() {
    objective = new NumberOfVisitedEndangeredSitesObjective();
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MAXIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_endangered_visited_sites_is_empty() {
    final var matrix = new TravelMatrix(new ArrayList<>());
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_value_when_visited_sites_are_all_endangered() {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var start = TravelStartLocation.builder().coordinates(0, 0).build();
    final var matrix = new TravelMatrix(sites, start);
    final var endangeredSite = sites.get(0);
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(endangeredSite)
            .build(matrix);
    assertEquals(1L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void test_get_objective_value_when_visited_sites_are_not_all_endangered() {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var start = TravelStartLocation.builder().coordinates(0, 0).build();
    final var matrix = new TravelMatrix(sites, start);
    final var endangeredSite = sites.get(0);
    final var notEndangeredSite = sites.get(1);
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(endangeredSite)
            .visitedSite(notEndangeredSite)
            .build(matrix);
    assertEquals(1L, objective.computeObjectiveValue(solution).getValue());
  }
}
