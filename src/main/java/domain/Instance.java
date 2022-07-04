package domain;

import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.matrix.TravelMatrix;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Instance {

  final List<Site> sites;
  final TravelMatrix matrix;
  final TravelStartLocation start;
}
