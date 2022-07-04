package domain.objectives.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import domain.objectives.interfaces.Objective;
import domain.objectives.interfaces.VisitNewSiteObjective;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectiveHolderTest {

  private Objective objective1, objective2;
  private ObjectiveHolder objectiveHolder;

  @BeforeEach
  public void setUp() {
    objective1 = mock(Objective.class, withSettings().extraInterfaces(VisitNewSiteObjective.class));
    objective2 = mock(Objective.class);
    objectiveHolder =
        ObjectiveHolder.builder().objective(objective1).objective(objective2).build();
  }

  @Test
  public void test_constructor() {
    // Generic objectives
    final var objectives = objectiveHolder.getObjectives();
    assertEquals(2, objectives.size());
    assertTrue(objectives.contains(objective1));
    assertTrue(objectives.contains(objective2));
    // Visit new site objectives
    final var visitNewSiteObjectives = objectiveHolder.getVisitNewSiteObjectives();
    assertEquals(1, visitNewSiteObjectives.size());
    assertTrue(visitNewSiteObjectives.contains((VisitNewSiteObjective) objective1));
  }

}
