package optimisation.choosers.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SequentialSiteFilterTest {

  private Solution solution;
  private Site siteFilter1And2, siteOnlyFilter1, otherSite;
  private List<Site> sites;
  private SequentialSiteFilter filter;

  @BeforeEach
  public void setUp() {
    solution = mock(Solution.class);
    siteFilter1And2 = mock(Site.class);
    siteOnlyFilter1 = mock(Site.class);
    otherSite = mock(Site.class);
    sites = Arrays.asList(siteFilter1And2, siteOnlyFilter1, otherSite);
    final var filter1 = mock(SiteFilter.class);
    when(filter1.siteHasInterest(solution, siteFilter1And2)).thenReturn(true);
    when(filter1.siteHasInterest(solution, siteOnlyFilter1)).thenReturn(true);
    when(filter1.siteHasInterest(solution, otherSite)).thenReturn(false);
    final var filter2 = mock(SiteFilter.class);
    when(filter2.siteHasInterest(solution, siteFilter1And2)).thenReturn(true);
    when(filter2.siteHasInterest(solution, siteOnlyFilter1)).thenReturn(false);
    when(filter2.siteHasInterest(solution, otherSite)).thenReturn(false);
    filter = SequentialSiteFilter.builder().filter(filter1).filter(filter2).build();
  }

  @Test
  public void test_site_has_interest() {
    assertTrue(filter.siteHasInterest(solution, siteFilter1And2));
    assertFalse(filter.siteHasInterest(solution, siteOnlyFilter1));
    assertFalse(filter.siteHasInterest(solution, otherSite));
  }

  @Test
  public void test_filter() {
    final var expectedResult = Collections.singletonList(siteFilter1And2);
    assertEquals(expectedResult, filter.filter(solution, sites));
  }
}
