package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.Solution.SolutionBuilder;
import domain.matrix.TravelMatrix;
import domain.site.Coordinates;
import domain.site.SiteReader;
import domain.site.SiteReaderTest;
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
  public void test_get_name() {
    assertEquals(ObjectiveName.NUMBER_OF_VISITED_COUNTRIES, objective.getObjectiveName());
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
    final var matrix = new TravelMatrix(sites);
    final var afghanSite = sites.get(0);
    final var algerianSite = sites.get(1);
    final var solution =
        new SolutionBuilder()
            .start(new Coordinates(0, 0))
            .visitedSite(afghanSite)
            .visitedSite(algerianSite)
            .build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }

  @Test
  public void
      test_get_objective_value_when_list_of_visited_countries_contains_different_number_of_sites_and_countries() {
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var matrix = new TravelMatrix(sites);
    final var twoCountriesSite = sites.get(2);
    final var solution =
        new SolutionBuilder()
            .start(new Coordinates(0, 0))
            .visitedSite(twoCountriesSite)
            .build(matrix);
    assertEquals(2L, objective.computeObjectiveValue(solution).getValue());
  }
}
