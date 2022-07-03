package optimisation.choosers.filters;

import static org.mockito.Mockito.mock;

import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.solution.Solution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CulturalSiteFilterTest {

  private Site cultural, natural;
  private CulturalSiteFilter filter;
  private Solution solution;

  @BeforeEach
  public void setUp() {
    cultural = Site.builder().type(SiteType.Cultural).build();
    natural = Site.builder().type(SiteType.Natural).build();
    filter = new CulturalSiteFilter();
    solution = mock(Solution.class);
  }

  @Test
  public void site_has_interest_returns_true() {
    Assertions.assertTrue(filter.siteHasInterest(solution, cultural));
  }

  @Test
  public void site_has_interest_returns_false() {
    Assertions.assertFalse(filter.siteHasInterest(solution, natural));
  }

}
