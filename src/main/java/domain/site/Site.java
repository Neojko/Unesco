package domain.site;

import com.google.errorprone.annotations.Immutable;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@Immutable
@EqualsAndHashCode
public class Site {

  private final String name;
  private final Coordinates coordinates;
  private final List<Country> countries;
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
