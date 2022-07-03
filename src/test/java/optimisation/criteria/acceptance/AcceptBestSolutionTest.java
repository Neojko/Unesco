package optimisation.criteria.acceptance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.solution.Solution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AcceptBestSolutionTest {

  private Solution solution1, solution2;
  private AcceptBestSolution acceptBestSolution;

  @BeforeEach
  public void setUp() {
    solution1 = mock(Solution.class);
    solution2 = mock(Solution.class);
    acceptBestSolution = new AcceptBestSolution();
  }

  @Test
  public void test_accept_returns_true() {
    when(solution1.isBetterThan(solution2)).thenReturn(true);
    assertTrue(acceptBestSolution.accept(solution1, solution2));
  }

  @Test
  public void test_accept_returns_false() {
    when(solution1.isBetterThan(solution2)).thenReturn(false);
    assertFalse(acceptBestSolution.accept(solution1, solution2));
  }
}
