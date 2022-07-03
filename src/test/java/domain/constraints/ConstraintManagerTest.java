package domain.constraints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import domain.constraints.interfaces.Constraint;
import domain.constraints.interfaces.VisitNewSiteConstraint;
import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ConstraintManagerTest {

  private Constraint constraint1, constraint2, constraint3;
  private ConstraintManager constraintManager;

  @BeforeEach
  public void setUp() {
    constraint1 =
        mock(Constraint.class, withSettings().extraInterfaces(VisitNewSiteConstraint.class));
    constraint2 =
        mock(Constraint.class, withSettings().extraInterfaces(VisitNewSiteConstraint.class));
    constraint3 = mock(Constraint.class);
    constraintManager =
        ConstraintManager.builder()
            .constraint(constraint1)
            .constraint(constraint2)
            .constraint(constraint3)
            .build();
  }

  @Test
  public void test_constructor() {
    // Generic constraints
    assertEquals(3, constraintManager.getConstraints().size());
    assertTrue(constraintManager.getConstraints().contains(constraint1));
    assertTrue(constraintManager.getConstraints().contains(constraint2));
    assertTrue(constraintManager.getConstraints().contains(constraint3));
    // Visit new site constraints
    assertEquals(2, constraintManager.getVisitNewSiteConstraints().size());
    assertTrue(constraintManager.getVisitNewSiteConstraints().contains(constraint1));
    assertTrue(constraintManager.getVisitNewSiteConstraints().contains(constraint2));
  }

  private static Stream<Arguments> test_compute_is_feasible() {
    return Stream.of(
        Arguments.of(true, true, true, true), // all constraints are feasible
        Arguments.of(false, true, true, false), // first constraint not feasible
        Arguments.of(true, false, true, false), // second constraint not feasible
        Arguments.of(true, false, true, false), // third constraint not feasible
        Arguments.of(false, false, false, false) // no feasible constraint
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_compute_is_feasible(
      final boolean constraint1IsFeasible,
      final boolean constraint2IsFeasible,
      final boolean constraint3IsFeasible,
      final boolean expectedResult) {
    final var solution = mock(Solution.class);
    when(constraint1.isFeasible(solution)).thenReturn(constraint1IsFeasible);
    when(constraint2.isFeasible(solution)).thenReturn(constraint2IsFeasible);
    when(constraint3.isFeasible(solution)).thenReturn(constraint3IsFeasible);
    assertEquals(expectedResult, constraintManager.isFeasible(solution));
  }

  private static Stream<Arguments> test_can_visit_new_site() {
    return Stream.of(
        Arguments.of(true, true, true), // both constraints are feasible
        Arguments.of(false, true, false), // first constraint not feasible
        Arguments.of(true, false, false), // second constraint not feasible
        Arguments.of(false, false, false) // no feasible constraint
        );
  }

  // constraint1 and constraint2 are VisitNewSiteConstraints (not constraint3)
  @ParameterizedTest
  @MethodSource
  public void test_can_visit_new_site(
      final boolean constraint1IsFeasible,
      final boolean constraint2IsFeasible,
      final boolean expectedResult) {
    final var solution = mock(Solution.class);
    final var site = mock(Site.class);
    final var position = 1;
    final var constraint1AsVisitNewSiteConstraint = (VisitNewSiteConstraint) constraint1;
    final var constraint2AsVisitNewSiteConstraint = (VisitNewSiteConstraint) constraint2;
    final long increase = 1;
    when(constraint1AsVisitNewSiteConstraint.canVisitNewSite(solution, site, position, increase))
        .thenReturn(constraint1IsFeasible);
    when(constraint2AsVisitNewSiteConstraint.canVisitNewSite(solution, site, position, increase))
        .thenReturn(constraint2IsFeasible);
    final var result = constraintManager.canVisitNewSite(solution, site, position, increase);
    assertEquals(expectedResult, result);
  }
}
