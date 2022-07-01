package domain.objectives.components;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ObjectiveValueTest {

  private ObjectiveValue maxWithValue10, minWithValue10;

  @BeforeEach
  public void setUp() {
    maxWithValue10 = ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(10L).build();
    minWithValue10 = ObjectiveValue.builder().sense(ObjectiveSense.MINIMIZE).value(10L).build();
  }

  @Test
  public void test_constructor_when_maximising_with_value_zero() {
    assertEquals(0L, ObjectiveValue.ZERO_MAX_OBJECTIVE_VALUE.getValue());
    assertEquals(ObjectiveSense.MAXIMIZE, ObjectiveValue.ZERO_MAX_OBJECTIVE_VALUE.getSense());
  }

  @Test
  public void test_constructor_when_minimising_with_value_zero() {
    assertEquals(0L, ObjectiveValue.ZERO_MIN_OBJECTIVE_VALUE.getValue());
    assertEquals(ObjectiveSense.MINIMIZE, ObjectiveValue.ZERO_MIN_OBJECTIVE_VALUE.getSense());
  }

  @Test
  public void test_constructor_when_maximising_with_worst_possible_value() {
    assertEquals(Long.MIN_VALUE, ObjectiveValue.WORST_MAX_OBJECTIVE_VALUE.getValue());
    assertEquals(ObjectiveSense.MAXIMIZE, ObjectiveValue.WORST_MAX_OBJECTIVE_VALUE.getSense());
  }

  @Test
  public void test_constructor_when_minimising_with_worst_possible_value() {
    assertEquals(Long.MAX_VALUE, ObjectiveValue.WORST_MIN_OBJECTIVE_VALUE.getValue());
    assertEquals(ObjectiveSense.MINIMIZE, ObjectiveValue.WORST_MIN_OBJECTIVE_VALUE.getSense());
  }

  private static Stream<Arguments> test_compare_to_when_maximising() {
    return Stream.of(Arguments.of(9L, -1), Arguments.of(10L, 0), Arguments.of(11L, 1));
  }

  @ParameterizedTest
  @MethodSource
  public void test_compare_to_when_maximising(
      final long valueOfOtherObjectiveValue, final int expectedResult) {
    final var other =
        ObjectiveValue.builder()
            .value(valueOfOtherObjectiveValue)
            .sense(ObjectiveSense.MAXIMIZE)
            .build();
    assertEquals(expectedResult, maxWithValue10.compareTo(other));
  }

  private static Stream<Arguments> test_compare_to_when_minimising() {
    return Stream.of(Arguments.of(9L, 1), Arguments.of(10L, 0), Arguments.of(11L, -1));
  }

  @ParameterizedTest
  @MethodSource
  public void test_compare_to_when_minimising(
      final long valueOfOtherObjectiveValue, final int expectedResult) {
    final var other =
        ObjectiveValue.builder()
            .value(valueOfOtherObjectiveValue)
            .sense(ObjectiveSense.MINIMIZE)
            .build();
    assertEquals(expectedResult, minWithValue10.compareTo(other));
  }

  @Test
  public void test_sum_when_minimising() {
    final var otherObjectiveValue =
        ObjectiveValue.builder().value(3L).sense(ObjectiveSense.MINIMIZE).build();
    final var sum = minWithValue10.sum(otherObjectiveValue);

    assertEquals(ObjectiveSense.MINIMIZE, sum.getSense());
    assertEquals(13L, sum.getValue());
  }

  @Test
  public void test_sum_when_maximising() {
    final var otherObjectiveValue =
        ObjectiveValue.builder().value(4L).sense(ObjectiveSense.MAXIMIZE).build();
    final var sum = maxWithValue10.sum(otherObjectiveValue);

    assertEquals(ObjectiveSense.MAXIMIZE, sum.getSense());
    assertEquals(14L, sum.getValue());
  }

  @Test
  public void test_multiply_when_minimising() {
    final var product = minWithValue10.multiply(3);
    assertEquals(ObjectiveSense.MINIMIZE, product.getSense());
    assertEquals(30, product.getValue());
  }

  @Test
  public void test_multiply_when_maximising() {
    final var product = maxWithValue10.multiply(3);
    assertEquals(ObjectiveSense.MAXIMIZE, product.getSense());
    assertEquals(30, product.getValue());
  }

  @Test
  public void test_copy_when_minimising() {
    final var copiedObjectiveValue = minWithValue10.copy();
    assertEquals(minWithValue10, copiedObjectiveValue);

    // Modify minWithValue10's value and check that copiedObjectiveValue is unaffected
    minWithValue10 = minWithValue10.sum(minWithValue10);

    assertEquals(10L, copiedObjectiveValue.getValue());
    assertEquals(ObjectiveSense.MINIMIZE, copiedObjectiveValue.getSense());
    assertEquals(20L, minWithValue10.getValue());
    assertEquals(ObjectiveSense.MINIMIZE, minWithValue10.getSense());
  }

  @Test
  public void test_copy_when_maximising() {
    final var copiedObjectiveValue = maxWithValue10.copy();
    assertEquals(maxWithValue10, copiedObjectiveValue);

    // Modify maxWithValue10's value and check that copiedObjectiveValue is unaffected
    maxWithValue10 = maxWithValue10.sum(maxWithValue10);

    assertEquals(10L, copiedObjectiveValue.getValue());
    assertEquals(ObjectiveSense.MAXIMIZE, copiedObjectiveValue.getSense());
    assertEquals(20L, maxWithValue10.getValue());
    assertEquals(ObjectiveSense.MAXIMIZE, maxWithValue10.getSense());
  }
}
