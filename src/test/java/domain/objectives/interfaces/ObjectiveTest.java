package domain.objectives.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.ObjectiveValue;
import org.junit.jupiter.api.Test;

public class ObjectiveTest {

  @Test
  public void test_get_zero_max_objective_value() {
    final Objective objective = mock(Objective.class);
    when(objective.getObjectiveSense()).thenReturn(ObjectiveSense.MAXIMIZE);
    when(objective.getZeroObjectiveValue()).thenCallRealMethod();
    assertEquals(ObjectiveValue.ZERO_MAX_OBJECTIVE_VALUE, objective.getZeroObjectiveValue());
  }

  @Test
  public void test_get_zero_min_objective_value() {
    final Objective objective = mock(Objective.class);
    when(objective.getObjectiveSense()).thenReturn(ObjectiveSense.MINIMIZE);
    when(objective.getZeroObjectiveValue()).thenCallRealMethod();
    assertEquals(ObjectiveValue.ZERO_MIN_OBJECTIVE_VALUE, objective.getZeroObjectiveValue());
  }

  @Test
  public void test_get_worst_max_objective_value() {
    final Objective objective = mock(Objective.class);
    when(objective.getObjectiveSense()).thenReturn(ObjectiveSense.MAXIMIZE);
    when(objective.getWorstObjectiveValue()).thenCallRealMethod();
    assertEquals(ObjectiveValue.WORST_MAX_OBJECTIVE_VALUE, objective.getWorstObjectiveValue());
  }

  @Test
  public void test_get_worst_min_objective_value() {
    final Objective objective = mock(Objective.class);
    when(objective.getObjectiveSense()).thenReturn(ObjectiveSense.MINIMIZE);
    when(objective.getWorstObjectiveValue()).thenCallRealMethod();
    assertEquals(ObjectiveValue.WORST_MIN_OBJECTIVE_VALUE, objective.getWorstObjectiveValue());
  }
}
