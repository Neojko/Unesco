package domain.matrix;

import domain.site.Coordinates;
import lombok.var;

public class HaversineComputer {

  /** @return Haversine distance in kilometers between two Coordinates */
  public static double getDistance(final Coordinates first, final Coordinates second) {
    final var latDistance = toRadius(second.getLatitude() - first.getLatitude());
    final var lonDistance = toRadius(second.getLongitude() - first.getLongitude());
    final var a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(toRadius(first.getLatitude()))
            * Math.cos(toRadius(second.getLatitude()))
            * Math.sin(lonDistance / 2)
            * Math.sin(lonDistance / 2);
    final var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return 6371.009 * c;
  }

  private static double toRadius(final double value) {
    return value * Math.PI / 180;
  }

}
