package optimisation.moves;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.constraints.ConstraintManager;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.objectives.components.ObjectiveValues;
import domain.solution.Solution;
import domain.solution.SolutionTripDurationComputer;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class UnvisitSiteMoveTest {

  private boolean isFeasible;
  private long tripDurationDelta;
  private ObjectiveValues objectiveValues;
  private UnvisitSiteMove move;

  @BeforeEach
  public void setUp() {
    final var solution = mock(Solution.class);
    final var site = mock(Site.class);
    final TravelMatrix matrix = mock(TravelMatrix.class);
    isFeasible = true;
    tripDurationDelta = 1L;
    objectiveValues = mock(ObjectiveValues.class);
    try (MockedStatic<SolutionTripDurationComputer> mockDelta =
        Mockito.mockStatic(SolutionTripDurationComputer.class)) {
      mockDelta
          .when(
              () ->
                  SolutionTripDurationComputer.computeTripDurationDeltaToUnvisitSite(
                      solution, site, matrix))
          .thenReturn(tripDurationDelta);
      final ConstraintManager constraintManager = mock(ConstraintManager.class);
      when(constraintManager.canUnvisitSite(solution, site)).thenReturn(isFeasible);
      final ObjectiveManager objectiveManager = mock(ObjectiveManager.class);
      when(objectiveManager.computeUnvisitSiteObjectiveValuesDelta(
              solution, site, tripDurationDelta))
          .thenReturn(objectiveValues);
      move = new UnvisitSiteMove(solution, site, matrix, constraintManager, objectiveManager);
    }
  }

  @Test
  public void test_is_feasible() {
    assertEquals(isFeasible, move.isFeasible());
  }

  @Test
  public void test_get_trip_duration_delta() {
    assertEquals(tripDurationDelta, move.getTripDurationDelta());
  }

  @Test
  public void test_get_objective_values_delta() {
    assertEquals(objectiveValues, move.getObjectiveValuesDelta());
  }
}
