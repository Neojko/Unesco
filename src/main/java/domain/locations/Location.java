package domain.locations;

public interface Location {

  LocationID getLocationID();

  Coordinates getCoordinates();

  default boolean isSite() {
    return false;
  }
}
