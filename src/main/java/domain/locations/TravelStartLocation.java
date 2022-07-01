package domain.locations;

import com.google.errorprone.annotations.Immutable;
import lombok.Builder;

@Builder
@Immutable
public class TravelStartLocation implements Location {

  // Using value -1 because no Unesco site will have this LocationID
  private final static LocationID locationID = new LocationID(-1);

  private final Coordinates coordinates;

  @Override
  public LocationID getLocationID() {
    return locationID;
  }

  @Override
  public Coordinates getCoordinates() {
    return coordinates;
  }

  public static class TravelStartLocationBuilder {
    public TravelStartLocationBuilder coordinates(final double latitude, final double longitude) {
      coordinates = new Coordinates(latitude, longitude);
      return this;
    }
    public TravelStartLocationBuilder coordinates(final Coordinates coordinates) {
      this.coordinates = coordinates;
      return this;
    }
  }
}
