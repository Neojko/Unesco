package domain;

import domain.matrix.TravelMatrix;
import domain.matrix.computers.TravelTimeComputer;
import domain.site.Coordinates;
import domain.site.Country;
import domain.site.Site;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.var;

@Getter
public class Solution {

  private final Coordinates start;
  private final List<Site> visitedSites;
  private final List<Site> unvisitedSites;
  private final Map<Country, List<Site>> visitedCountries;
  private final long tripTime;

  private Solution(
      final Coordinates start,
      final List<Site> visitedSites,
      final List<Site> unvisitedSites,
      final Map<Country, List<Site>> visitedCountries,
      final long tripTime) {
    this.start = start;
    this.visitedSites = visitedSites;
    this.unvisitedSites = unvisitedSites;
    this.visitedCountries = visitedCountries;
    this.tripTime = tripTime;
  }

  public boolean isVisitingSite(final Site site) {
    return visitedSites.contains(site);
  }

  public boolean isVisitingCountry(final Country country) {
    return visitedCountries.containsKey(country);
  }

  public static class SolutionBuilder {
    private Coordinates start;
    private final List<Site> visitedSites;
    private final List<Site> unvisitedSites;
    private final Map<Country, List<Site>> visitedCountries;

    public SolutionBuilder() {
      visitedSites = new ArrayList<>();
      unvisitedSites = new ArrayList<>();
      visitedCountries = new HashMap<>();
    }

    public SolutionBuilder start(final Coordinates start) {
      this.start = start;
      return this;
    }

    /** Adds site at the end of the list of visited sites */
    public SolutionBuilder visitedSite(final Site visitedSite) {
      visitedSites.add(visitedSite);
      for (final var country : visitedSite.getCountries()) {
        if (!visitedCountries.containsKey(country)) {
          visitedCountries.put(country, new ArrayList<>());
        }
        final var countrySites = visitedCountries.get(country);
        countrySites.add(visitedSite);
      }
      return this;
    }

    /** Adds site at the end of the list of unvisited sites (even if order does not matter here) */
    public SolutionBuilder unvisitedSite(final Site unvisitedSite) {
      unvisitedSites.add(unvisitedSite);
      return this;
    }

    /**
     * Returns solution with accurate visited countries and current trip time
     *
     * @param matrix: must contain all visited and (preferably) unvisited sites
     */
    public Solution build(final TravelMatrix matrix) {
      return new Solution(
          start, visitedSites, unvisitedSites, visitedCountries, computeTripTime(matrix));
    }

    private long computeTripTime(final TravelMatrix matrix) {
      if (visitedSites.isEmpty()) {
        return 0L;
      }
      long result = TravelTimeComputer.convertToTime(start, visitedSites.get(0).getCoordinates());
      for (int i = 0; i < visitedSites.size() - 1; i++) {
        result += matrix.time(visitedSites.get(i), visitedSites.get(i + 1));
      }
      final var lastVisitedSite = visitedSites.get(visitedSites.size() - 1);
      result += TravelTimeComputer.convertToTime(lastVisitedSite.getCoordinates(), start);
      return result;
    }
  }
}
