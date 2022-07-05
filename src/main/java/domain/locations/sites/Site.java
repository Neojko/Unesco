package domain.locations.sites;

import com.google.errorprone.annotations.Immutable;
import domain.locations.Coordinates;
import domain.locations.Location;
import domain.locations.LocationID;
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
public class Site implements Location {

  private final String name;
  private final LocationID locationID;
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

  @Override
  public String toString() {
    final StringBuilder result = new StringBuilder();
    result.append(name).append(" / ");
    result.append(countries.get(0));
    if (countries.size() > 1) {
      for (int i = 1; i < countries.size() - 1; i++) {
        final var country = countries.get(i);
        result.append(", ").append(country.toString());
      }
      final var lastCountry = countries.get(countries.size() - 1);
      result.append(" and ").append(lastCountry.toString());
    }
    result.append(" / ");
    result.append(type.toString());
    if (isEndangered) {
      result.append(" / Endangered");
    }
    return result.toString();
  }

  public static class SiteBuilder {
    public SiteBuilder locationID(final int locationID) {
      this.locationID = new LocationID(locationID);
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
