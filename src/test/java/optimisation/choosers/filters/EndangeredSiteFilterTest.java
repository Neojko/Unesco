package optimisation.choosers.filters;

import static org.mockito.Mockito.mock;

import domain.locations.sites.Site;
import domain.solution.Solution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EndangeredSiteFilterTest {

  private Site endangeredSite, notEndangeredSite;
  private EndangeredSiteFilter filter;
  private Solution solution;

  @BeforeEach
  public void setUp() {
    endangeredSite = Site.builder().isEndangered().build();
    notEndangeredSite = Site.builder().build();
    filter = new EndangeredSiteFilter();
    solution = mock(Solution.class);
  }

  @Test
  public void site_has_interest_returns_true() {
    Assertions.assertTrue(filter.siteHasInterest(solution, endangeredSite));
  }

  @Test
  public void site_has_interest_returns_false() {
    Assertions.assertFalse(filter.siteHasInterest(solution, notEndangeredSite));
  }

}
