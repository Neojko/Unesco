package domain.locations;

import com.google.errorprone.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Immutable
public class Coordinates {
  private final double latitude;
  private final double longitude;

  @Override
  public String toString() {
    return latitude + ", " + longitude;
  }
}
