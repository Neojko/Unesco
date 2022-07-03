package optimisation.choosers.filters;

import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.solution.Solution;
import java.util.Arrays;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SitesWithNotVisitedCountriesFilterTest {

  private Site otherFrenchSite;
  private Site frenchAndEnglishSite;
  private SiteWithNotVisitedCountriesFilter filter;
  private Solution solution;

  @BeforeEach
  public void setUp() {
    filter = new SiteWithNotVisitedCountriesFilter();
    final var coordinates = new Coordinates(0, 0);
    final var start = TravelStartLocation.builder().coordinates(coordinates).build();
    final var france = new Country("France");
    final var england = new Country("England");
    final Site frenchSite =
        Site.builder()
            .locationID(1)
            .coordinates(coordinates)
            .country(france)
            .type(SiteType.Natural)
            .build();
    otherFrenchSite =
        Site.builder()
            .locationID(2)
            .coordinates(coordinates)
            .country(france)
            .type(SiteType.Natural)
            .build();
    frenchAndEnglishSite =
        Site.builder()
            .locationID(3)
            .coordinates(coordinates)
            .country(france)
            .country(england)
            .type(SiteType.Natural)
            .build();
    final var locations = Arrays.asList(start, frenchSite, otherFrenchSite, frenchAndEnglishSite);
    final var matrix = new TravelMatrix(locations);
    solution =
        Solution.builder()
            .start(start)
            .visitedSite(frenchSite)
            .unvisitedSite(otherFrenchSite)
            .unvisitedSite(frenchAndEnglishSite)
            .build(matrix);
  }

  @Test
  public void site_has_interest_returns_true() {
    Assertions.assertTrue(filter.siteHasInterest(solution, frenchAndEnglishSite));
  }

  @Test
  public void site_has_interest_returns_false() {
    Assertions.assertFalse(filter.siteHasInterest(solution, otherFrenchSite));
  }
}
