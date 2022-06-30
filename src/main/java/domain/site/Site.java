package domain.site;

import com.google.errorprone.annotations.Immutable;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
@Immutable
@EqualsAndHashCode
public class Site {

  private final String name;
  private final SiteNumber uniqueNumber;
  private final Coordinates coordinates;
  @Singular private final List<Country> countries;
  private final SiteType type;
  private final boolean isEndangered;

  public boolean isCultural() {
    return type.isCultural();
  }

  public boolean isNatural() {
    return type.isNatural();
  }

  public static class SiteBuilder {
    public SiteBuilder uniqueNumber(final int uniqueNumber) {
      this.uniqueNumber = new SiteNumber(uniqueNumber);
      return this;
    }

    public SiteBuilder isEndangered() {
      this.isEndangered = true;
      return this;
    }

    public SiteBuilder isEndangered(final boolean value) {
      this.isEndangered = value;
      return this;
    }
  }
}
