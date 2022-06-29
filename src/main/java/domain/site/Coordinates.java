package domain.site;

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
}
