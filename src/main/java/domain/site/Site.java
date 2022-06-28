package domain.site;

import com.google.errorprone.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Immutable
public class Site {

  private final City city;
  private final Country country;
  private final SiteStatus status;

  public boolean isCultural() {
    return status.isCultural();
  }

  public boolean isNatural() {
    return status.isNatural();
  }

  public boolean isEndangered() {
    return status.isEndangered();
  }

}
