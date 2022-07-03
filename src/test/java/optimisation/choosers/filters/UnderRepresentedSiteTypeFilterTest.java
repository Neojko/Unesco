package optimisation.choosers.filters;

import domain.Solution;
import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import java.util.Arrays;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnderRepresentedSiteTypeFilterTest {

  private Site cultural1, cultural2, natural1, natural2;
  private UnderRepresentedSiteTypeFilter filter;
  private Solution solution;
  private TravelStartLocation start;
  private TravelMatrix matrix;

  @BeforeEach
  public void setUp() {
    filter = new UnderRepresentedSiteTypeFilter();
    final var coordinates = new Coordinates(0, 0);
    start = TravelStartLocation.builder().coordinates(coordinates).build();
    final var france = new Country("France");
    cultural1 = Site.builder()
        .locationID(1)
        .coordinates(coordinates)
        .country(france)
        .type(SiteType.Cultural)
        .build();
    cultural2 = Site.builder()
        .locationID(2)
        .coordinates(coordinates)
        .country(france)
        .type(SiteType.Cultural)
        .build();
    natural1 = Site.builder()
        .locationID(3)
        .coordinates(coordinates)
        .country(france)
        .type(SiteType.Natural)
        .build();
    natural2 = Site.builder()
        .locationID(4)
        .coordinates(coordinates)
        .country(france)
        .type(SiteType.Natural)
        .build();
    final var locations = Arrays.asList(cultural1, cultural2, natural1, natural2, start);
    matrix = new TravelMatrix(locations);
  }

  @Test
  public void cultural_site_has_interest_returns_true_when_more_natural() {
    solution = Solution.builder()
        .start(start)
        .visitedSite(natural1)
        .unvisitedSite(cultural1)
        .build(matrix);
    Assertions.assertTrue(filter.siteHasInterest(solution, cultural1));
  }

  @Test
  public void cultural_site_has_interest_returns_true_when_same_number_of_cultural_and_natural() {
    solution = Solution.builder()
        .start(start)
        .visitedSite(natural1)
        .visitedSite(cultural1)
        .unvisitedSite(cultural2)
        .build(matrix);
    Assertions.assertTrue(filter.siteHasInterest(solution, cultural2));
  }

  @Test
  public void cultural_site_has_interest_returns_false_when_more_cultural() {
    solution = Solution.builder()
        .start(start)
        .visitedSite(cultural1)
        .unvisitedSite(cultural2)
        .build(matrix);
    Assertions.assertFalse(filter.siteHasInterest(solution, cultural2));
  }

  @Test
  public void natural_site_has_interest_returns_false_when_more_natural() {
    solution = Solution.builder()
        .start(start)
        .visitedSite(natural1)
        .unvisitedSite(natural2)
        .build(matrix);
    Assertions.assertFalse(filter.siteHasInterest(solution, natural2));
  }

  @Test
  public void natural_site_has_interest_returns_true_when_same_number_of_cultural_and_natural() {
    solution = Solution.builder()
        .start(start)
        .visitedSite(natural1)
        .visitedSite(cultural1)
        .unvisitedSite(natural2)
        .build(matrix);
    Assertions.assertTrue(filter.siteHasInterest(solution, natural2));
  }

  @Test
  public void natural_site_has_interest_returns_true_when_more_cultural() {
    solution = Solution.builder()
        .start(start)
        .visitedSite(cultural1)
        .unvisitedSite(natural1)
        .build(matrix);
    Assertions.assertTrue(filter.siteHasInterest(solution, natural1));
  }

}
