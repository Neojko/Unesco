package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Solution.SolutionBuilder;
import domain.locations.Coordinates;
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

public class NumberOfVisitedCountriesObjectiveTest {

  private Objective objective;

  @BeforeEach
  public void setUp() {
    objective = new NumberOfVisitedCountriesObjective();
  }

  @Test
  public void test_get_sense() {
    assertEquals(ObjectiveSense.MAXIMIZE, objective.getObjectiveSense());
  }

  @Test
  public void test_get_objective_value_when_list_of_visited_countries_is_empty() {
    final var matrix = new TravelMatrix(new ArrayList<>());
    final var solution = new SolutionBuilder().build(matrix);
    assertEquals(0L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void
      test_get_objective_value_when_list_of_visited_countries_contains_same_number_of_sites_and_countries() {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var start = TravelStartLocation.builder().coordinates(new Coordinates(0, 0)).build();
    final var matrix = new TravelMatrix(sites, start);
    final var afghanSite = sites.get(0);
    final var algerianSite = sites.get(1);
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(afghanSite)
            .visitedSite(algerianSite)
            .build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void
      test_get_objective_value_when_list_of_visited_countries_contains_different_number_of_sites_and_countries() {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var start = TravelStartLocation.builder().coordinates(new Coordinates(0, 0)).build();
    final var matrix = new TravelMatrix(sites, start);
    final var twoCountriesSite = sites.get(2);
    final var solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(twoCountriesSite)
            .build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }
}
