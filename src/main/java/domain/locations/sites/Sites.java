package domain.locations.sites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.var;

@EqualsAndHashCode
@Getter
public class Sites {

  private final List<Site> sites;
  private final Map<Country, List<Site>> sitesPerCountry;
  private int numberOfCulturalSites;
  private int numberOfNaturalSites;

  private Sites(final List<Site> sites, final Map<Country, List<Site>> sitesPerCountry,
      final int numberOfCulturalSites, final int numberOfNaturalSites) {
    this.sites = sites;
    this.sitesPerCountry = sitesPerCountry;
    this.numberOfCulturalSites = numberOfCulturalSites;
    this.numberOfNaturalSites = numberOfNaturalSites;
  }

  public static SitesBuilder builder() { return new SitesBuilder(); }

  public boolean containsSite(final Site site) {
    return sites.contains(site);
  }

  public boolean containsCountry(final Country country) {
    return sitesPerCountry.containsKey(country);
  }

  public List<Site> getSites(final Country country) {
    return sitesPerCountry.get(country);
  }

  public void addSite(final Site site, final int position) {
    sites.add(position, site);
    updateSitesPerCountryWhenAddingSite(site);
    if (site.isCultural()) {
      numberOfCulturalSites++;
    }
    if (site.isNatural()) {
      numberOfNaturalSites++;
    }
  }

  private void updateSitesPerCountryWhenAddingSite(final Site site) {
    for (final var country : site.getCountries()) {
      if (!sitesPerCountry.containsKey(country)) {
        sitesPerCountry.put(country, new ArrayList<>());
      }
      final var countrySites = sitesPerCountry.get(country);
      countrySites.add(site);
    }
  }

  public void removeSite(final Site site) {
    sites.remove(site);
    updateSitesPerCountryWhenRemovingSite(site);
    if (site.isCultural()) {
      numberOfCulturalSites++;
    }
    if (site.isNatural()) {
      numberOfNaturalSites++;
    }
  }

  private void updateSitesPerCountryWhenRemovingSite(final Site site) {
    for (final var country : site.getCountries()) {
      final var countrySites = sitesPerCountry.get(country);
      countrySites.remove(site);
      if (countrySites.isEmpty()) {
        sitesPerCountry.remove(country);
      }
    }
  }

  public Sites copy() {
    return new Sites(
        new ArrayList<>(this.sites),
        new HashMap<>(this.sitesPerCountry),
        numberOfCulturalSites,
        numberOfNaturalSites
    );
  }

  public static class SitesBuilder {
    private final List<Site> sites;
    private final Map<Country, List<Site>> countries;

    public SitesBuilder() {
      sites = new ArrayList<>();
      countries = new HashMap<>();
    }

    /** Adds site at the end of the list of sites */
    public SitesBuilder site(final Site site) {
      sites.add(site);
      for (final var country : site.getCountries()) {
        if (!countries.containsKey(country)) {
          countries.put(country, new ArrayList<>());
        }
        final var countrySites = countries.get(country);
        countrySites.add(site);
      }
      return this;
    }

    public Sites build() {
      return new Sites(
          sites,
          countries,
          computeNumberOfCulturalSites(),
          computeNumberOfNaturalSites());
    }

    private int computeNumberOfCulturalSites() {
      return (int) sites.stream().filter(Site::isCultural).count();
    }

    private int computeNumberOfNaturalSites() {
      return (int) sites.stream().filter(Site::isNatural).count();
    }
  }

}
