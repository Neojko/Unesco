package optimisation.choosers.filters;

import static org.mockito.Mockito.mock;

import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.solution.Solution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NaturalSiteFilterTest {

  private Site cultural, natural;
  private NaturalSiteFilter filter;
  private Solution solution;

  @BeforeEach
  public void setUp() {
    cultural = Site.builder().type(SiteType.Cultural).build();
    natural = Site.builder().type(SiteType.Natural).build();
    filter = new NaturalSiteFilter();
    solution = mock(Solution.class);
  }

  @Test
  public void site_has_interest_returns_true() {
    Assertions.assertTrue(filter.siteHasInterest(solution, natural));
  }

  @Test
  public void site_has_interest_returns_false() {
    Assertions.assertFalse(filter.siteHasInterest(solution, cultural));
  }
}
