package optimisation.choosers.filters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.locations.sites.Site;
import domain.solution.Solution;
import java.util.Arrays;
import java.util.Collections;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SiteFilterTest {

  @Test
  public void test_filter() {
    final var site1 = mock(Site.class);
    final var site2 = mock(Site.class);
    final var sites = Arrays.asList(site1, site2);
    final SiteFilter filter = mock(SiteFilter.class);
    final Solution solution = mock(Solution.class);
    when(filter.siteHasInterest(solution, site1)).thenReturn(true);
    when(filter.siteHasInterest(solution, site2)).thenReturn(false);
    when(filter.filter(solution, sites)).thenCallRealMethod();
    final var expectedResult = Collections.singletonList(site1);
    Assertions.assertEquals(expectedResult, filter.filter(solution, sites));
  }

}
