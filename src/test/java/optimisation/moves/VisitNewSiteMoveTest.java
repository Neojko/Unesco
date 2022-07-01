package optimisation.moves;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.Solution;
import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import domain.site.Site;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VisitNewSiteMoveTest {

  private boolean isFeasible;
  private ObjectiveValues objectiveValues;
  private VisitNewSiteMove move;

  @BeforeEach
  public void setUp() {
    final var solution = mock(Solution.class);
    final var site = mock(Site.class);
    final var position = 2;
    isFeasible = true;
    objectiveValues = mock(ObjectiveValues.class);
    final TravelMatrix matrix = mock(TravelMatrix.class);
    final ConstraintManager constraintManager = mock(ConstraintManager.class);
    when(constraintManager.canVisitNewSite(solution, site, position, matrix))
        .thenReturn(isFeasible);
    final ObjectiveManager objectiveManager = mock(ObjectiveManager.class);
    when(objectiveManager.computeVisitNewSiteObjectiveValuesDelta(solution, site))
        .thenReturn(objectiveValues);
    move =
        new VisitNewSiteMove(solution, site, position, matrix, constraintManager, objectiveManager);
  }

  @Test
  public void test_is_feasible() {
    assertEquals(isFeasible, move.isFeasible());
  }

  @Test
  public void test_get_objective_values_delta() {
    assertEquals(objectiveValues, move.getObjectiveValuesDelta());
  }
}
