package domain.objectives;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import domain.locations.sites.Site;
import domain.objectives.components.ObjectiveValue;
import domain.objectives.components.ObjectiveValues;
import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import domain.solution.Solution;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectiveManagerTest {

  private Objective objective1, objective2;
  private ObjectiveManager objectiveManager;

  @BeforeEach
  public void setUp() {
    objective1 = mock(Objective.class, withSettings().extraInterfaces(VisitNewSiteObjective.class));
    objective2 = mock(Objective.class);
    objectiveManager =
        ObjectiveManager.builder().objective(objective1).objective(objective2).build();
  }

  @Test
  public void test_constructor() {
    // Generic objectives
    assertEquals(2, objectiveManager.getObjectives().size());
    assertTrue(objectiveManager.getObjectives().contains(objective1));
    assertTrue(objectiveManager.getObjectives().contains(objective2));
    // Visit new site objectives
    assertEquals(1, objectiveManager.getVisitNewSiteObjectives().size());
    assertTrue(objectiveManager.getVisitNewSiteObjectives().contains(objective1));
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
    assertEquals(expectedObjectiveValues, objectiveManager.computeObjectiveValues(solution));
  }

  // objective1 is a VisitNewSiteObjective (not objective2)
  @Test
  public void test_compute_visit_new_site_objective_values_delta() {
    final var solution = mock(Solution.class);
    final var site = mock(Site.class);
    final var objective1AsVisitNewSiteObjective = (VisitNewSiteObjective) objective1;
    final var objectiveValue1 = ObjectiveValue.WORST_MAX_OBJECTIVE_VALUE;
    final long tripDurationDelta = 1L;
    when(objective1AsVisitNewSiteObjective.getVisitNewSiteObjectiveValueDelta(
            solution, site, tripDurationDelta))
        .thenReturn(objectiveValue1);
    final var expectedObjectiveValues =
        ObjectiveValues.builder().value(objective1, objectiveValue1).build();
    final var result =
        objectiveManager.computeVisitNewSiteObjectiveValuesDelta(solution, site, tripDurationDelta);
    assertEquals(expectedObjectiveValues, result);
  }
}
