package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.Solution;
import domain.objectives.components.Objective;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.components.ObjectiveValues;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectiveManagerTest {

  private Objective objective1, objective2;
  private ObjectiveManager objectiveManager;

  @BeforeEach
  public void setUp() {
    objective1 = mock(Objective.class);
    objective2 = mock(Objective.class);
    objectiveManager =
        ObjectiveManager.builder().objective(objective1).objective(objective2).build();
  }

  @Test
  public void test_constructor() {
    assertEquals(2, objectiveManager.getObjectives().size());
    assertTrue(objectiveManager.getObjectives().contains(objective1));
    assertTrue(objectiveManager.getObjectives().contains(objective2));
  }

  @Test
  public void test_compute_objective_values() {
    final var objectiveValue1 = ObjectiveValue.WORST_MAX_OBJECTIVE_VALUE;
    final var objectiveValue2 = ObjectiveValue.WORST_MIN_OBJECTIVE_VALUE;
    final var solution = mock(Solution.class);
    when(objective1.computeObjectiveValue(solution)).thenReturn(objectiveValue1);
    when(objective2.computeObjectiveValue(solution)).thenReturn(objectiveValue2);
    final var expectedObjectiveValues =
        ObjectiveValues.builder()
            .value(objective1, objectiveValue1)
            .value(objective2, objectiveValue2)
            .build();
    assertEquals(expectedObjectiveValues, objectiveManager.computeObjectivesValues(solution));
  }
}
