package domain.objectives;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/** An ObjectiveValue object contains the value of an Objective object. */
@Builder
@EqualsAndHashCode
@Getter
public class ObjectiveValue {

  public static final ObjectiveValue ZERO_MAX_OBJECTIVE_VALUE =
      ObjectiveValue.builder().value(0L).sense(ObjectiveSense.MAXIMIZE).build();

  public static final ObjectiveValue ZERO_MIN_OBJECTIVE_VALUE =
      ObjectiveValue.builder().value(0L).sense(ObjectiveSense.MINIMIZE).build();

  public static final ObjectiveValue WORST_MAX_OBJECTIVE_VALUE =
      ObjectiveValue.builder().value(Long.MIN_VALUE).sense(ObjectiveSense.MAXIMIZE).build();

  public static final ObjectiveValue WORST_MIN_OBJECTIVE_VALUE =
      ObjectiveValue.builder().value(Long.MAX_VALUE).sense(ObjectiveSense.MINIMIZE).build();

  private final long value;
  private final ObjectiveSense sense;

  /** @return new ObjectiveValue with the sum of the two values */
  public ObjectiveValue plus(final ObjectiveValue other) {
    return ObjectiveValue.builder().value(value + other.value).sense(sense).build();
  }

  public boolean isBetterThan(final ObjectiveValue other) {
    if (sense.equals(ObjectiveSense.MAXIMIZE)) {
      return value > other.value;
    } else {
      return value < other.value;
    }
  }

  public ObjectiveValue copy() {
    return ObjectiveValue.builder().value(value).sense(sense).build();
  }
}
