package domain.objectives.components;

import static domain.objectives.components.ObjectiveSense.MAXIMIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import domain.objectives.NumberOfVisitedCountriesObjective;
import domain.objectives.NumberOfVisitedSitesObjective;
import domain.objectives.Objective;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ObjectiveValuesComparatorTest {

  private Objective parity, visitedSites, visitedCountries, visitedEndangered;

  @BeforeEach
  public void setUp() {
    parity = new NumberOfVisitedSitesObjective();
    visitedSites = new NumberOfVisitedCountriesObjective();
  }

  @ParameterizedTest(
      name =
          "ObjectiveValues1Value1 = {0}, "
              + "ObjectiveValues2Value1 = {1}, "
              + "ObjectiveValues1Value2 = {2}, "
              + "ObjectiveValues2Value2 = {3}, "
              + "expectedResult = {4}")
  @CsvSource({
      // first ObjectiveValues has better first comparison
      "1, 0, 0, 0, -1",
      // second ObjectiveValues has better first comparison
      "0, 1, 0, 0, 1",
      // first ObjectiveValues has better second comparison (first is same)
      "0, 0, 1, 0, -1",
      // second ObjectiveValues has better second comparison (first is same)
      "0, 0, 0, 1, 1",
      // both ObjectiveValues have the same values
      "0, 0, 0, 0, 0"
  })
  public void test_compare_to(
      final long valueParity1,
      final long valueParity2,
      final long valueVisitedSites1,
      final long valueVisitedSites2,
      final long valueVisitedCountries1,
      final long valueVisitedCountries2,
      final long valueVisitedEndangeredSites1,
      final long valueVisitedEndangeredSites2,
      final int expectedResult) {
    final var objectiveValues = create(valueParity1, valueVisitedSites1, valueVisitedCountries1,
        valueVisitedEndangeredSites1);
    final var otherObjectiveValues = create(valueParity2, valueVisitedSites2,
        valueVisitedCountries2, valueVisitedEndangeredSites2);
    final var comparator = new ObjectiveValuesComparator();
    assertEquals(expectedResult, comparator.compare(objectiveValues, otherObjectiveValues));
  }

  private ObjectiveValues create(
      final long valueParity,
      final long valueVisitedSites,
      final long valueVisitedCountries,
      final long valueVisitedEndangered
  ) {
    return ObjectiveValues.builder()
        .value(parity, new ObjectiveValue(valueParity, MAXIMIZE))
        .value(visitedSites, new ObjectiveValue(valueVisitedSites, MAXIMIZE))
        .value(visitedCountries, new ObjectiveValue(valueVisitedCountries, MAXIMIZE))
        .value(visitedEndangered, new ObjectiveValue(valueVisitedEndangered, MAXIMIZE))
        .build();
  }

}
