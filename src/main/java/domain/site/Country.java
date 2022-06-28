package domain.site;

import com.google.errorprone.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Immutable
public class Country {

  private final String name;

  @Override
  public String toString() {
    return name;
  }

}
