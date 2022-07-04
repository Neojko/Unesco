package optimisation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.Instance;
import domain.constraints.ConstraintManager;
import domain.locations.TravelStartLocation;
import domain.locations.sites.SiteReader;
import domain.locations.sites.SiteReaderTest;
import domain.matrix.TravelMatrix;
import domain.objectives.ObjectiveManager;
import domain.solution.Solution;
import lombok.var;
import optimisation.algorithms.Algorithm;
import optimisation.modificators.repairers.Repairer;
import org.junit.jupiter.api.Test;

public class SolverTest {

  @Test
  public void test_solve() {
    final var constraintManager = ConstraintManager.builder().build();
    final var objectiveManager = ObjectiveManager.builder().build();
    final var sites = new SiteReader().createSites(SiteReaderTest.testFile);
    final var start = TravelStartLocation.builder().coordinates(0, 0).build();
    final var matrix = new TravelMatrix(sites, start);
    final Instance instance = Instance.builder().start(start).sites(sites).matrix(matrix).build();
    final var algorithm = mock(Algorithm.class);
    final var solution = mock(Solution.class);
    when(algorithm.improve(any(), any(), any(), any())).thenReturn(solution);
    final var initialRepairer = mock(Repairer.class);
    final var solver =
        Solver.builder()
            .constraintManager(constraintManager)
            .objectiveManager(objectiveManager)
            .algorithm(algorithm)
            .instance(instance)
            .initialRepairer(initialRepairer)
            .build();
    assertEquals(solution, solver.solve());
  }
}
