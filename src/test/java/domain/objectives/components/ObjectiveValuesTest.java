package domain.objectives.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.objectives.NumberOfVisitedCountriesObjective;
import domain.objectives.NumberOfVisitedSitesObjective;
import domain.objectives.Objective;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ObjectiveValuesTest {

  private Objective objective1, objective2;
  private ObjectiveSense sense;
  private List<Objective> objectives;

  @BeforeEach
  public void setUp() {
    objective1 = new NumberOfVisitedSitesObjective();
    objective2 = new NumberOfVisitedCountriesObjective();
    sense = ObjectiveSense.MAXIMIZE; // both objectives have this sense, tests will be more compact
    objectives = new ArrayList<>(Arrays.asList(objective1, objective2));
  }

  @Test
  public void test_create_empty_objectives() {
    final var objectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    assertEquals(2, objectiveValues.getObjectives().size());
    assertTrue(objectiveValues.getObjectives().containsAll(objectives));
    assertEquals(0, objectiveValues.getObjectiveValuesMap().keySet().size());
  }

  @Test
  public void test_create_zero_objective_values() {
    final var objectiveValues = ObjectiveValues.createZeroObjectiveValues(objectives);
    assertEquals(2, objectiveValues.getObjectives().size());
    assertTrue(objectiveValues.getObjectives().containsAll(objectives));
    assertEquals(2, objectiveValues.getObjectiveValuesMap().keySet().size());
    assertEquals(
        objective1.getZeroObjectiveValue(),
        objectiveValues.getObjectiveValuesMap().get(objective1));
    assertEquals(
        objective2.getZeroObjectiveValue(),
        objectiveValues.getObjectiveValuesMap().get(objective2));
  }

  @Test
  public void test_create_worst_objective_values() {
    final var objectiveValues = ObjectiveValues.createWorstObjectiveValues(objectives);
    assertEquals(2, objectiveValues.getObjectives().size());
    assertTrue(objectiveValues.getObjectives().containsAll(objectives));
    assertEquals(2, objectiveValues.getObjectiveValuesMap().keySet().size());
    assertEquals(
        objective1.getWorstObjectiveValue(),
        objectiveValues.getObjectiveValuesMap().get(objective1));
    assertEquals(
        objective2.getWorstObjectiveValue(),
        objectiveValues.getObjectiveValuesMap().get(objective2));
  }

  @Test
  public void test_set_objective_value() {
    final var objectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    assertFalse(objectiveValues.getObjectiveValuesMap().containsKey(objective1));
    objectiveValues.set(objective1, new ObjectiveValue(1L, sense));
    assertTrue(objectiveValues.getObjectiveValuesMap().containsKey(objective1));
    assertEquals(1L, objectiveValues.getObjectiveValuesMap().get(objective1).getValue());
  }

  @ParameterizedTest(
      name =
          "ObjectiveValues1Value1 = {0}, "
              + "ObjectiveValues2Value1 = {1}, "
              + "ObjectiveValues1Value2 = {2}, "
              + "ObjectiveValues2Value2 = {3}, "
              + "expectedResult = {4}")
  @CsvSource({
    "1, 0, 0, 0, true", // first ObjectiveValues has better first comparison
    "0, 1, 0, 0, false", // second ObjectiveValues has better first comparison
    "0, 0, 1, 0, true", // first ObjectiveValues has better second comparison (first is same)
    "0, 0, 0, 0, false", // second ObjectiveValues has better second comparison (first is same)
    "0, 0, 0, 0, false", // both ObjectiveValues have the same values
  })
  public void test_is_better_than(
      final long objectiveValues1Value1,
      final long objectiveValues2Value1,
      final long objectiveValues1Value2,
      final long objectiveValues2Value2,
      final boolean expectedResult) {
    final var objectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    objectiveValues.set(objective1, new ObjectiveValue(objectiveValues1Value1, sense));
    objectiveValues.set(objective2, new ObjectiveValue(objectiveValues1Value2, sense));

    final var otherObjectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    otherObjectiveValues.set(objective1, new ObjectiveValue(objectiveValues2Value1, sense));
    otherObjectiveValues.set(objective2, new ObjectiveValue(objectiveValues2Value2, sense));

    assertEquals(expectedResult, objectiveValues.isBetterThan(otherObjectiveValues));
  }

  @Test
  public void test_plus() {
    final var objectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    objectiveValues.set(objective1, ObjectiveValue.builder().value(2L).build());
    objectiveValues.set(objective2, ObjectiveValue.builder().value(3L).build());

    final var otherObjectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    otherObjectiveValues.set(objective1, ObjectiveValue.builder().value(4L).build());
    otherObjectiveValues.set(objective2, ObjectiveValue.builder().value(5L).build());

    objectiveValues.add(otherObjectiveValues);

    assertEquals(6L, objectiveValues.getObjectiveValuesMap().get(objective1).getValue());
    assertEquals(8L, objectiveValues.getObjectiveValuesMap().get(objective2).getValue());
  }

  @Test
  public void test_copy_empty_objective_values() {
    final var objectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);

    final var copy = objectiveValues.copy();
    assertEquals(objectiveValues, copy);

    objectiveValues.set(objective1, ObjectiveValue.builder().value(1L).build());
    assertNotEquals(objectiveValues, copy);
    assertFalse(copy.getObjectiveValuesMap().containsKey(objective1));
  }

  @Test
  public void copy_withObjectiveValues_test() {
    final ObjectiveValues objectiveValues = ObjectiveValues.createEmptyObjectiveValues(objectives);
    objectiveValues.set(objective1, ObjectiveValue.builder().value(2L).build());
    objectiveValues.set(objective2, ObjectiveValue.builder().value(3L).build());

    final var copy = objectiveValues.copy();
    assertEquals(objectiveValues, copy);

    objectiveValues.set(objective1, ObjectiveValue.builder().value(1L).build());
    assertNotEquals(objectiveValues, copy);
    assertEquals(2L, copy.getObjectiveValuesMap().get(objective1).getValue());
  }
}
