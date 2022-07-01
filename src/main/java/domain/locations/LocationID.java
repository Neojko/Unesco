package domain.locations;

import com.google.errorprone.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Location unique ID
 */

@Getter
@EqualsAndHashCode
@Immutable
@AllArgsConstructor
public class LocationID {
  private final int value;
}
