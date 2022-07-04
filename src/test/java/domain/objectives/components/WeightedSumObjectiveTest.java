package domain.objectives.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import domain.locations.sites.Site;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeightedSumObjectiveTest {

  private Objective objective1, objective2;
  private ObjectiveSense sense;
  private long weight1, weight2;
  private WeightedSumObjective weightedSumObjective;

  @BeforeEach
  public void setUp() {
    objective1 = mock(Objective.class, withSettings().extraInterfaces(VisitNewSiteObjective.class));
    objective2 = mock(Objective.class);
    sense = ObjectiveSense.MAXIMIZE;
    weight1 = 2;
    weight2 = 3;
    weightedSumObjective = WeightedSumObjective.builder()
        .sense(sense)
        .objective(objective1, weight1).objective(objective2, weight2)
        .build();
  }

  @Test
  public void test_constructor() {
    assertEquals(sense, weightedSumObjective.getSense());
    final var expectedObjectiveHolder = ObjectiveHolder.builder()
        .objective(objective1).objective(objective2)
        .build();
    assertEquals(expectedObjectiveHolder, weightedSumObjective.getObjectiveHolder());
    assertEquals(2, weightedSumObjective.getWeights().size());
    assertEquals(weight1, weightedSumObjective.getWeight(objective1));
    assertEquals(weight2, weightedSumObjective.getWeight(objective2));
  }

  @Test
  public void test_compute_objective_value() {
    final var solution = mock(Solution.class);
    final var value1 = 1L;
    final var value2 = 4L;
    final var objectiveValue1 = ObjectiveValue.builder().sense(sense).value(value1).build();
    final var objectiveValue2 = ObjectiveValue.builder().sense(sense).value(value2).build();
    when(objective1.computeObjectiveValue(any(Solution.class))).thenReturn(objectiveValue1);
    when(objective2.computeObjectiveValue(any(Solution.class))).thenReturn(objectiveValue2);
    final var expectedValue = value1 * weight1 + value2 * weight2;
    final var expectedObjectiveValue = ObjectiveValue.builder()
        .sense(sense)
        .value(expectedValue)
        .build();
    assertEquals(expectedObjectiveValue, weightedSumObjective.computeObjectiveValue(solution));
  }

  @Test
  public void test_compute_objective_value_delta_when_visiting_new_site() {
    final var solution = mock(Solution.class);
    final var site = mock(Site.class);
    final var delta = 0;
    final var value1 = 1L;
    final var objectiveValue1 = ObjectiveValue.builder().sense(sense).value(value1).build();
    final var objective1AsVisitNewSiteObjective = (VisitNewSiteObjective) objective1;
    when(objective1AsVisitNewSiteObjective.getVisitNewSiteObjectiveValueDelta(solution, site, delta)
    ).thenReturn(objectiveValue1);
    final var expectedValue = value1 * weight1;
    final var expectedObjectiveValue = ObjectiveValue.builder()
        .sense(sense)
        .value(expectedValue)
        .build();
    assertEquals(
        expectedObjectiveValue,
        weightedSumObjective.getVisitNewSiteObjectiveValueDelta(solution, site, delta));
  }

}
