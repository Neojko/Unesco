package optimisation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.objectives.components.ObjectiveValues;
import java.util.stream.Stream;
import lombok.var;
import optimisation.moves.Move;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MoveTest {

  private static Stream<Arguments> test_is_better_than() {
    return Stream.of(
        Arguments.of(false, false, -1, false), // Not feasible
        Arguments.of(true, false, -1, true), // Other is not feasible
        Arguments.of(true, true, -1, true), // Both feasible with compareTo = -1
        Arguments.of(true, true, 0, false), // Both feasible with compareTo = 0
        Arguments.of(true, true, 1, false) // Both feasible with compareTo = 1
        );
  }

  @ParameterizedTest
  @MethodSource
  public void test_is_better_than(
      final boolean isFeasible,
      final boolean otherIsFeasible,
      final int compareTo,
      final boolean expectedResult) {
    final var move = mock(Move.class);
    final var otherMove = mock(Move.class);
    final var moveObjectiveValuesDelta = mock(ObjectiveValues.class);
    final var otherMoveObjectiveValuesDelta = mock(ObjectiveValues.class);
    when(move.isFeasible()).thenReturn(isFeasible);
    when(otherMove.isFeasible()).thenReturn(otherIsFeasible);
    when(move.getObjectiveValuesDelta()).thenReturn(moveObjectiveValuesDelta);
    when(otherMove.getObjectiveValuesDelta()).thenReturn(otherMoveObjectiveValuesDelta);
    when(moveObjectiveValuesDelta.compareTo(otherMoveObjectiveValuesDelta)).thenReturn(compareTo);
    when(move.isBetterThan(otherMove)).thenCallRealMethod();
    assertEquals(expectedResult, move.isBetterThan(otherMove));
  }
}
