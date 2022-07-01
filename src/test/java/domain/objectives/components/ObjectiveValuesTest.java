package domain.objectives.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.objectives.NumberOfVisitedCountriesObjective;
import domain.objectives.NumberOfVisitedSitesObjective;
import domain.objectives.interfaces.Objective;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ObjectiveValuesTest {

  private Objective objective1, objective2;
  private ObjectiveRanking objectiveRanking;

  @BeforeEach
  public void setUp() {
    objective1 = new NumberOfVisitedSitesObjective();
    objective2 = new NumberOfVisitedCountriesObjective();
    objectiveRanking =
        ObjectiveRanking.builder().objective(objective1).objective(objective2).build();
  }

  @Test
  public void test_create_zero_objective_values() {
    final var objectiveValues = ObjectiveValues.createZeroObjectiveValues(objectiveRanking);
    assertEquals(2, objectiveValues.getValues().keySet().size());
    assertEquals(objective1.getZeroObjectiveValue(), objectiveValues.getValues().get(objective1));
    assertEquals(objective2.getZeroObjectiveValue(), objectiveValues.getValues().get(objective2));
  }

  @Test
  public void test_create_worst_objective_values() {
    final var objectiveValues = ObjectiveValues.createWorstObjectiveValues(objectiveRanking);
    assertEquals(2, objectiveValues.getValues().keySet().size());
    assertEquals(objective1.getWorstObjectiveValue(), objectiveValues.getValues().get(objective1));
    assertEquals(objective2.getWorstObjectiveValue(), objectiveValues.getValues().get(objective2));
  }

  @Test
  public void test_set_objective_value() {
    final var objectiveValues = ObjectiveValues.builder().build();
    final var objectiveValue = new ObjectiveValue(1L, ObjectiveSense.MAXIMIZE);
    assertFalse(objectiveValues.getValues().containsKey(objective1));
    objectiveValues.set(objective1, objectiveValue);
    assertTrue(objectiveValues.getValues().containsKey(objective1));
    assertEquals(objectiveValue, objectiveValues.getValues().get(objective1));
  }

  @Test
  public void test_plus() {
    final var objectiveValues = withValues(2L, 3L);
    final var otherObjectiveValues = withValues(4L, 5L);

    objectiveValues.add(otherObjectiveValues);
    assertEquals(6L, objectiveValues.getValues().get(objective1).getValue());
    assertEquals(8L, objectiveValues.getValues().get(objective2).getValue());
  }

  private static Stream<Arguments> test_compare_to() {
    return Stream.of(
        Arguments.of(1, 0, 0, 0, -1), // better first objective value
        Arguments.of(0, 1, 0, 0, 1), // worst first objective value
        Arguments.of(1, 1, 1, 0, -1), // same first and better second objective value
        Arguments.of(1, 1, 0, 1, 1), // same first and worst second objective value
        Arguments.of(1, 1, 1, 1, 0) // same objective values
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_compare_to(
      final long objective1value1,
      final long objective1value2,
      final long objective2value1,
      final long objective2value2,
      final int expectedResult) {
    final var objectiveValues = withValues(objective1value1, objective2value1);
    final var otherObjectiveValues = withValues(objective1value2, objective2value2);
    assertEquals(expectedResult, objectiveValues.compareTo(otherObjectiveValues));
  }

  @Test
  public void test_copy_empty_objective_values() {
    final var objectiveValues = ObjectiveValues.builder().build();
    final var copy = objectiveValues.copy();
    assertEquals(objectiveValues, copy);

    objectiveValues.set(objective1, ObjectiveValue.builder().value(1L).build());
    assertNotEquals(objectiveValues, copy);
    assertFalse(copy.getValues().containsKey(objective1));
  }

  @Test
  public void copy_with_non_empty_objective_values() {
    final var objectiveValues = withValues(2L, 3L);
    final var copy = objectiveValues.copy();
    assertEquals(objectiveValues, copy);

    objectiveValues.set(objective1, ObjectiveValue.builder().value(1L).build());
    assertNotEquals(objectiveValues, copy);
    assertEquals(2L, copy.getValues().get(objective1).getValue());
  }

  private ObjectiveValues withValues(final long value1, final long value2) {
    return ObjectiveValues.builder()
        .value(
            objective1,
            ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(value1).build())
        .value(
            objective2,
            ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(value2).build())
        .ranking(objectiveRanking)
        .build();
  }
}
