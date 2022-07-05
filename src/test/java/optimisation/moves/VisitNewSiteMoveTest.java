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

public class VisitNewSiteMoveTest {

  private boolean isFeasible;
  private long tripDurationDelta;
  private ObjectiveValues objectiveValues;
  private VisitNewSiteMove move;

  @BeforeEach
  public void setUp() {
    final var solution = mock(Solution.class);
    final var site = mock(Site.class);
    final var position = 2;
    final TravelMatrix matrix = mock(TravelMatrix.class);
    isFeasible = true;
    tripDurationDelta = 1L;
    objectiveValues = mock(ObjectiveValues.class);
    final ConstraintManager constraintManager = mock(ConstraintManager.class);
    try (MockedStatic<SolutionTripDurationComputer> mockDelta =
        Mockito.mockStatic(SolutionTripDurationComputer.class)) {
      mockDelta
          .when(
              () ->
                  SolutionTripDurationComputer.computeTripDurationDeltaToVisitNewSite(
                      solution, site, position, matrix))
          .thenReturn(tripDurationDelta);
      when(constraintManager.canVisitNewSite(solution, site, position, tripDurationDelta))
          .thenReturn(isFeasible);
      final ObjectiveManager objectiveManager = mock(ObjectiveManager.class);
      when(objectiveManager.computeVisitNewSiteObjectiveValuesDelta(
              solution, site, tripDurationDelta))
          .thenReturn(objectiveValues);
      move =
          new VisitNewSiteMove(
              solution, site, position, matrix, constraintManager, objectiveManager);
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
