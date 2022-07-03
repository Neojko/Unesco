package domain.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.constraints.ConstraintManager;
import domain.locations.Coordinates;
import domain.locations.Location;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Country;
import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.matrix.TravelMatrix;
import domain.objectives.NumberOfVisitedEndangeredSitesObjective;
import domain.objectives.NumberOfVisitedSitesObjective;
import domain.objectives.ObjectiveManager;
import domain.objectives.interfaces.Objective;
import domain.solution.Solution.SolutionBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.var;
import optimisation.moves.VisitNewSiteMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SolutionTest {

  private TravelStartLocation start;
  private Country france, england, spain, germany;
  private Site site1, site2, site3, site4;
  private TravelMatrix matrix;
  private Solution solution;
  private ConstraintManager constraintManager;
  private ObjectiveManager objectiveManager;

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
    final Objective visitNewSitesObjective = new NumberOfVisitedSitesObjective();
    final Objective visitEndangeredSitesObjective = new NumberOfVisitedEndangeredSitesObjective();
    final var solutionBuilder =
        new SolutionBuilder()
            .start(start)
            .visitedSite(site1)
            .visitedSite(site2)
            .visitedSite(site3)
            .unvisitedSite(site4)
            .objective(visitNewSitesObjective)
            .objective(visitEndangeredSitesObjective);
    constraintManager = solutionBuilder.getConstraintManager();
    objectiveManager = solutionBuilder.getObjectiveManager();
    solution = solutionBuilder.build(matrix);
  }

  @Test
  public void test_builder() {
    // Visited sites
    final var visitedSites = solution.getVisitedSites().getSites();
    assertEquals(3, visitedSites.size());
    assertTrue(visitedSites.contains(site1));
    assertTrue(visitedSites.contains(site2));
    assertTrue(visitedSites.contains(site3));
    // Unvisited sites
    final var unvisitedSites = solution.getUnvisitedSites().getSites();
    assertEquals(1, unvisitedSites.size());
    assertTrue(unvisitedSites.contains(site4));
    // Visited countries -- general
    final var countrySites = solution.getVisitedSites().getSitesPerCountry();
    assertEquals(3, countrySites.size());
    assertTrue(countrySites.containsKey(france));
    assertTrue(countrySites.containsKey(england));
    assertTrue(countrySites.containsKey(spain));
    assertFalse(countrySites.containsKey(germany));
    // Visited countries -- France
    final var frenchSites = countrySites.get(france);
    assertEquals(1, frenchSites.size());
    assertTrue(frenchSites.contains(site1));
    // Visited countries -- England
    final var englishSites = countrySites.get(england);
    assertEquals(2, englishSites.size());
    assertTrue(englishSites.contains(site1));
    assertTrue(englishSites.contains(site2));
    // Visited countries -- Spain
    final var spanishSites = countrySites.get(spain);
    assertEquals(2, spanishSites.size());
    assertTrue(spanishSites.contains(site2));
    assertTrue(spanishSites.contains(site3));
    // Visited countries -- Germany
    assertFalse(countrySites.containsKey(germany));
    // Trip time
    final var expectedTripDuration =
        matrix.time(start, site1)
            + matrix.time(site1, site2)
            + matrix.time(site2, site3)
            + matrix.time(site3, start)
            + 3 * SolutionTripDurationComputer.timePerSite;
    assertEquals(expectedTripDuration, solution.getTripDurationinSeconds());
    // Cultural sites
    assertEquals(2, solution.getVisitedSites().getNumberOfCulturalSites()); // site1 and site2
    // Natural sites
    assertEquals(2, solution.getVisitedSites().getNumberOfNaturalSites()); // site2 and site3
  }

  @Test
  public void test_copy() {
    final var otherSolution = solution.copy();
    assertEquals(solution, otherSolution);
    final var move =
        new VisitNewSiteMove(solution, site4, 0, matrix, constraintManager, objectiveManager);
    solution.apply(move);
    assertNotEquals(solution, otherSolution);
    assertNotEquals(solution.getVisitedSites(), otherSolution.getVisitedSites());
    assertNotEquals(solution.getUnvisitedSites(), otherSolution.getUnvisitedSites());
    assertNotEquals(solution.getTripDurationinSeconds(), otherSolution.getTripDurationinSeconds());
    assertNotEquals(solution.getObjectiveValues(), otherSolution.getObjectiveValues());
  }

  @Test
  public void test_apply_visit_new_site_move() {
    final var move =
        new VisitNewSiteMove(solution, site4, 0, matrix, constraintManager, objectiveManager);
    assertTrue(move.isFeasible());
    final var oldTripDuration = solution.getTripDurationinSeconds();
    final var oldObjectiveValues = solution.getObjectiveValues().copy();
    solution.apply(move);
    assertTrue(solution.getVisitedSites().containsSite(site4));
    assertFalse(solution.getUnvisitedSites().containsSite(site4));
    assertEquals(
        oldTripDuration + move.getTripDurationDelta(), solution.getTripDurationinSeconds());
    oldObjectiveValues.add(move.getObjectiveValuesDelta());
    ;
    assertEquals(oldObjectiveValues, solution.getObjectiveValues());
  }
}