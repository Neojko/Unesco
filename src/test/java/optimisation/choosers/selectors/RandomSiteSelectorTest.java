package optimisation.choosers.selectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import domain.locations.sites.Site;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.RandomNumberGenerator;

public class RandomSiteSelectorTest {

  private RandomSiteSelector selector;
  private Site site1;
  private List<Site> sites;

  @BeforeEach
  public void setUp() {
    final RandomNumberGenerator random = mock(RandomNumberGenerator.class);
    when(random.nextInt(any(Integer.class))).thenReturn(0);
    selector = new RandomSiteSelector(random);
    site1 = Site.builder().build();
    final Site site2 = Site.builder().build();
    sites = Arrays.asList(site1, site2);
  }

  @Test
  public void test_select() {
    Assertions.assertEquals(site1, selector.select(sites));
  }
}
