package optimisation.choosers.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AcceptAllFilterTest {

  private Solution solution;
  private Site site1;
  private List<Site> sites;
  private AcceptAllFilter filter;

  @BeforeEach
  public void setUp() {
    solution = mock(Solution.class);
    site1 = mock(Site.class);
    final Site site2 = mock(Site.class);
    sites = Arrays.asList(site1, site2);
    filter = new AcceptAllFilter();
  }

  @Test
  public void test_site_has_interest() {
    assertTrue(filter.siteHasInterest(solution, site1));
  }

  @Test
  public void test_filter() {
    assertEquals(sites, filter.filter(solution, sites));
  }
}
