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

public class ObjectiveValuesTest {

  private Objective objective1, objective2;
  private List<Objective> objectives;

  @BeforeEach
  public void setUp() {
    objective1 = new NumberOfVisitedSitesObjective();
    objective2 = new NumberOfVisitedCountriesObjective();
    objectives = new ArrayList<>(Arrays.asList(objective1, objective2));
  }

  @Test
  public void test_create_zero_objective_values() {
    final var objectiveValues = ObjectiveValues.createZeroObjectiveValues(objectives);
    assertEquals(2, objectiveValues.getValues().keySet().size());
    assertEquals(
        objective1.getZeroObjectiveValue(),
        objectiveValues.getValues().get(objective1));
    assertEquals(
        objective2.getZeroObjectiveValue(),
        objectiveValues.getValues().get(objective2));
  }

  @Test
  public void test_create_worst_objective_values() {
    final var objectiveValues = ObjectiveValues.createWorstObjectiveValues(objectives);
    assertEquals(2, objectiveValues.getValues().keySet().size());
    assertEquals(
        objective1.getWorstObjectiveValue(),
        objectiveValues.getValues().get(objective1));
    assertEquals(
        objective2.getWorstObjectiveValue(),
        objectiveValues.getValues().get(objective2));
  }

  @Test
  public void test_set_objective_value() {
    final var objectiveValues = ObjectiveValues.builder().build();
    assertFalse(objectiveValues.getValues().containsKey(objective1));
    objectiveValues.set(objective1, new ObjectiveValue(1L, ObjectiveSense.MAXIMIZE));
    assertTrue(objectiveValues.getValues().containsKey(objective1));
    assertEquals(1L, objectiveValues.getValues().get(objective1).getValue());
  }

  @Test
  public void test_plus() {
    final var objectiveValues = withValues(2L, 3L);
    final var otherObjectiveValues = withValues(4L, 5L);

    objectiveValues.add(otherObjectiveValues);
    assertEquals(6L, objectiveValues.getValues().get(objective1).getValue());
    assertEquals(8L, objectiveValues.getValues().get(objective2).getValue());
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
  public void copy_withObjectiveValues_test() {
    final var objectiveValues = withValues(2L, 3L);
    final var copy = objectiveValues.copy();
    assertEquals(objectiveValues, copy);

    objectiveValues.set(objective1, ObjectiveValue.builder().value(1L).build());
    assertNotEquals(objectiveValues, copy);
    assertEquals(2L, copy.getValues().get(objective1).getValue());
  }

  private ObjectiveValues withValues(final long value1, final long value2) {
    return ObjectiveValues.builder()
        .value(objective1, ObjectiveValue.builder().value(value1).build())
        .value(objective2, ObjectiveValue.builder().value(value2).build())
        .build();
  }
}
