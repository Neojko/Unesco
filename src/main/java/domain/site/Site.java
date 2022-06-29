package domain.site;

import com.google.errorprone.annotations.Immutable;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.var;

@Builder
@Getter
@Immutable
@EqualsAndHashCode
public class Site {

  private final String name;
  private final SiteNumber number;
  private final Coordinates coordinates;
  @Singular private final List<Country> countries;
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

  public static class SiteBuilder {
    public SiteBuilder number(final int number) {
      this.number = new SiteNumber(number);
      return this;
    }
  }

  public String toShortFormat() {
    final StringBuilder countryNames = new StringBuilder();
    countryNames.append("[");
    for (final var country : countries) {
      countryNames.append(country.getName()).append(";");
    }
    countryNames.deleteCharAt(countryNames.length()-1);
    countryNames.append("]");
    return name + "," + number.getNumber() + ","
        + coordinates.getLatitude() + "," + coordinates.getLongitude() + "," + countryNames + ","
        + status.isCultural() + "," + status.isNatural() + "," + status.isEndangered();
  }
}
