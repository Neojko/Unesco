package domain.locations.sites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SitesTest {

  private Country france, england, spain, germany;
  private Site frenchCulturalSite, englishNaturalSite, frenchAndSpanishMixedSite;

  @BeforeEach
  public void setUp() {
    france = new Country("France");
    england = new Country("England");
    spain = new Country("Splain");
    germany = new Country("Germany");
    frenchCulturalSite = Site.builder().type(SiteType.Cultural).country(france).build();
    englishNaturalSite = Site.builder().type(SiteType.Natural).country(england).build();
    frenchAndSpanishMixedSite =
        Site.builder().type(SiteType.Mixed).country(france).country(spain).build();
  }

  @Test
  public void test_constructor() {
    final var sites =
        Sites.builder()
            .site(frenchCulturalSite)
            .site(englishNaturalSite)
            .site(frenchAndSpanishMixedSite)
            .build();
    assertEquals(3, sites.getSites().size());
    assertEquals(frenchCulturalSite, sites.getSites().get(0));
    assertEquals(englishNaturalSite, sites.getSites().get(1));
    assertEquals(frenchAndSpanishMixedSite, sites.getSites().get(2));

    assertEquals(3, sites.getSitesPerCountry().size());

    assertTrue(sites.getSitesPerCountry().containsKey(france));
    final var frenchSites = sites.getSitesPerCountry().get(france);
    assertEquals(2, frenchSites.size());
    assertEquals(frenchCulturalSite, frenchSites.get(0));
    assertEquals(frenchAndSpanishMixedSite, frenchSites.get(1));

    assertTrue(sites.getSitesPerCountry().containsKey(england));
    final var englishSites = sites.getSitesPerCountry().get(england);
    assertEquals(1, englishSites.size());
    assertTrue(englishSites.contains(englishNaturalSite));

    assertTrue(sites.getSitesPerCountry().containsKey(spain));
    final var spanishSites = sites.getSitesPerCountry().get(spain);
    assertEquals(1, spanishSites.size());
    assertTrue(spanishSites.contains(frenchAndSpanishMixedSite));

    assertFalse(sites.getSitesPerCountry().containsKey(germany));

    assertEquals(2, sites.getNumberOfCulturalSites());
    assertEquals(2, sites.getNumberOfNaturalSites());
  }

  @Test
  public void test_contains_site_returns_true() {
    final var sites = Sites.builder().site(frenchCulturalSite).build();
    assertTrue(sites.containsSite(frenchCulturalSite));
  }

  @Test
  public void test_contains_site_returns_false() {
    final var sites = Sites.builder().build();
    assertFalse(sites.containsSite(frenchCulturalSite));
  }

  @Test
  public void test_contains_country_returns_true() {
    final var sites = Sites.builder().site(frenchCulturalSite).build();
    assertTrue(sites.containsCountry(france));
  }

  @Test
  public void test_contains_country_returns_false() {
    final var sites = Sites.builder().build();
    assertFalse(sites.containsCountry(france));
  }

  @Test
  public void test_add_site_with_new_country() {
    final var sites = Sites.builder().build();
    sites.addSite(frenchCulturalSite, 0);
    final var frenchSites = sites.getSitesPerCountry().get(france);
    assertEquals(1, frenchSites.size());
    assertTrue(frenchSites.contains(frenchCulturalSite));
  }

  @Test
  public void test_add_site_with_already_contained_country() {
    final var sites = Sites.builder().site(frenchCulturalSite).build();
    sites.addSite(frenchAndSpanishMixedSite, 0);
    final var frenchSites = sites.getSitesPerCountry().get(france);
    assertEquals(2, frenchSites.size());
    assertTrue(frenchSites.contains(frenchCulturalSite));
    assertTrue(frenchSites.contains(frenchAndSpanishMixedSite));
  }

  @Test
  public void test_remove_site_such_that_country_has_other_site() {
    final var sites =
        Sites.builder().site(frenchCulturalSite).site(frenchAndSpanishMixedSite).build();
    sites.removeSite(frenchCulturalSite);
    final var frenchSites = sites.getSitesPerCountry().get(france);
    assertEquals(1, frenchSites.size());
    assertTrue(frenchSites.contains(frenchAndSpanishMixedSite));
  }

  @Test
  public void test_remove_site_such_that_country_does_not_have_other_site() {
    final var sites = Sites.builder().site(frenchCulturalSite).build();
    sites.removeSite(frenchCulturalSite);
    assertFalse(sites.containsCountry(france));
  }

  @Test
  public void test_add_cultural_site() {
    final var sites = Sites.builder().build();
    final var oldValue = sites.getNumberOfCulturalSites();
    sites.addSite(frenchCulturalSite);
    assertEquals(oldValue + 1, sites.getNumberOfCulturalSites());
  }

  @Test
  public void test_add_natural_site() {
    final var sites = Sites.builder().build();
    final var oldValue = sites.getNumberOfNaturalSites();
    sites.addSite(englishNaturalSite);
    assertEquals(oldValue + 1, sites.getNumberOfNaturalSites());
  }

  @Test
  public void test_remove_cultural_site() {
    final var sites = Sites.builder().site(frenchCulturalSite).build();
    final var oldValue = sites.getNumberOfCulturalSites();
    sites.removeSite(frenchCulturalSite);
    assertEquals(oldValue - 1, sites.getNumberOfCulturalSites());
  }

  @Test
  public void test_remove_natural_site() {
    final var sites = Sites.builder().site(englishNaturalSite).build();
    final var oldValue = sites.getNumberOfNaturalSites();
    sites.removeSite(englishNaturalSite);
    assertEquals(oldValue - 1, sites.getNumberOfNaturalSites());
  }
}
