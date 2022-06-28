package domain.site;

import com.google.errorprone.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Immutable
public class City {

  private final String name;
  private final Coordinates coordinates;

  @Override
  public String toString() {
    return name;
  }

}
