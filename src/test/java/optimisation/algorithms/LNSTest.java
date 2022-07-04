package optimisation.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.constraints.ConstraintManager;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import java.util.stream.Stream;
import lombok.var;
import optimisation.criteria.acceptance.AcceptBestSolution;
import optimisation.criteria.stopping.NumberOfIterationsStoppingCriterion;
import optimisation.criteria.stopping.StoppingCriterion;
import optimisation.modificators.destroyers.Destroyer;
import optimisation.modificators.repairers.Repairer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LNSTest {

  private Destroyer destroyer;
  private Repairer repairer;
  private Solution solution, betterSolution;
  private StoppingCriterion stoppingCriterion;
  private TravelMatrix matrix;
  private ConstraintManager constraintManager;
  private ObjectiveManager objectiveManager;

  @BeforeEach
  public void setUp() {
    solution = mock(Solution.class);
    betterSolution = mock(Solution.class);
    when(solution.copy())
        .thenReturn(solution) // copy for current solution before loop
        .thenReturn(solution) // copy for best solution before loop
        .thenReturn(solution) // copy because solution is not accepted in first iteration
        .thenReturn(betterSolution); // copy for best solution in iteration 2 when new best is found
    when(solution.isBetterThan(any()))
        .thenReturn(false) // comparison with best solution in iteration 1
        .thenReturn(false) // acceptance criteria in iteration 1
        .thenReturn(true); // comparison with best solution in iteration 1
    destroyer = mock(Destroyer.class);
    repairer = mock(Repairer.class);
    matrix = mock(TravelMatrix.class);
    constraintManager = mock(ConstraintManager.class);
    objectiveManager = mock(ObjectiveManager.class);
  }

  private static Stream<Arguments> test_improve() {
    return Stream.of(Arguments.of(1), Arguments.of(2));
  }

  @ParameterizedTest
  @MethodSource
  public void test_improve(final int maxIterations) {
    final var lns = buildLNS(maxIterations);
    final var newSolution = lns.improve(constraintManager, objectiveManager, matrix, solution);
    assertEquals(getExpectedSolution(maxIterations), newSolution);
    assertTrue(stoppingCriterion.isMet());
  }

  private LNS buildLNS(final int maxIterations) {
    stoppingCriterion = new NumberOfIterationsStoppingCriterion(maxIterations);
    return LNS.builder()
        .stoppingCriterion(stoppingCriterion)
        .acceptanceCriterion(new AcceptBestSolution())
        .destroyer(destroyer)
        .repairer(repairer)
        .build();
  }

  // Algorithm will not find a best solution in iteration 1 but will find one in iteration 2
  private Solution getExpectedSolution(final int iterations) {
    return iterations == 1 ? solution : betterSolution;
  }
}
