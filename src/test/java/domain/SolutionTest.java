package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.Solution.SolutionBuilder;
import domain.locations.Coordinates;
import domain.locations.Location;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SolutionTest {

  private TravelStartLocation start;
  private Country france, england, spain, germany;
  private Site site1, site2, site3, site4;
  private TravelMatrix matrix;
  private Solution solution;

  @BeforeEach
  public void setUp() {
    start = TravelStartLocation.builder().coordinates(-83.839219, 87.234581).build();
    france = new Country("France");
    england = new Country("England");
    spain = new Country("spain");
    site1 =
        Site.builder()
            .locationID(1)
            .coordinates(new Coordinates(23.183948, 2.349192))
            .country(france)
            .country(england)
            .type(SiteType.Cultural)
            .build();
    site2 =
        Site.builder()
            .locationID(2)
            .coordinates(new Coordinates(81.183948, -35.349192))
            .country(england)
            .country(spain)
            .type(SiteType.Natural)
            .build();
    site3 =
        Site.builder()
            .locationID(3)
            .coordinates(new Coordinates(1.183948, 9.349192))
            .country(spain)
            .type(SiteType.Mixed)
            .build();
    site4 =
        Site.builder()
            .locationID(4)
            .coordinates(new Coordinates(15.183948, 4.349192))
            .country(germany)
            .type(SiteType.Natural)
            .build();
    final List<Location> locations =
        new ArrayList<>(Arrays.asList(site1, site2, site3, site4, start));
    matrix = new TravelMatrix(locations);
    solution =
        new SolutionBuilder()
            .start(start)
            .visitedSite(site1)
            .visitedSite(site2)
            .visitedSite(site3)
            .unvisitedSite(site4)
            .build(matrix);
  }

  @Test
  public void test_builder() {
    // Visited sites
    assertEquals(3, solution.getVisitedSites().size());
    assertTrue(solution.getVisitedSites().contains(site1));
    assertTrue(solution.getVisitedSites().contains(site2));
    assertTrue(solution.getVisitedSites().contains(site3));
    // Unvisited sites
    assertEquals(1, solution.getUnvisitedSites().size());
    assertTrue(solution.getUnvisitedSites().contains(site4));
    // Visited countries -- general
    assertEquals(3, solution.getVisitedCountries().size());
    assertTrue(solution.getVisitedCountries().containsKey(france));
    assertTrue(solution.getVisitedCountries().containsKey(england));
    assertTrue(solution.getVisitedCountries().containsKey(spain));
    assertFalse(solution.getVisitedCountries().containsKey(germany));
    // Visited countries -- France
    final var frenchSites = solution.getVisitedCountries().get(france);
    assertEquals(1, frenchSites.size());
    assertTrue(frenchSites.contains(site1));
    // Visited countries -- England
    final var englishSites = solution.getVisitedCountries().get(england);
    assertEquals(2, englishSites.size());
    assertTrue(englishSites.contains(site1));
    assertTrue(englishSites.contains(site2));
    // Visited countries -- Spain
    final var spanishSites = solution.getVisitedCountries().get(spain);
    assertEquals(2, spanishSites.size());
    assertTrue(spanishSites.contains(site2));
    assertTrue(spanishSites.contains(site3));
    // Visited countries -- Germany
    assertFalse(solution.getVisitedCountries().containsKey(germany));
    // Trip time
    final var expectedTripDuration =
        matrix.time(start, site1)
            + matrix.time(site1, site2)
            + matrix.time(site2, site3)
            + matrix.time(site3, start)
            + 3 * Solution.timePerSite;
    assertEquals(expectedTripDuration, solution.getDurationInSeconds());
    // Cultural sites
    assertEquals(2, solution.getNumberOfCulturalVisitedSites()); // site1 and site2
    // Natural sites
    assertEquals(2, solution.getNumberOfNaturalVisitedSites()); // site2 and site3
  }

  @Test
  public void test_is_visiting_site_returns_true() {
    assertTrue(solution.isVisitingSite(site1));
  }

  @Test
  public void test_is_visiting_site_returns_false() {
    assertFalse(solution.isVisitingSite(site4));
  }

  @Test
  public void test_is_visiting_country_returns_true() {
    assertTrue(solution.isVisitingCountry(france));
  }

  @Test
  public void test_is_visiting_country_returns_false() {
    assertFalse(solution.isVisitingCountry(germany));
  }

  @Test
  public void test_copy() {
    final var otherSolution = solution.copy();
    assertEquals(solution, otherSolution);
  }

  // TODO add new copy() tests when able to modify visited sites / countries / endangered sites
}
