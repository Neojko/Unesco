package domain.constraints;

import static org.mockito.Mockito.mock;

import domain.locations.sites.Site;
import domain.locations.sites.SiteType;
import domain.solution.Solution;
import org.junit.jupiter.api.BeforeEach;

public class LessThanXSiteTypeDifferenceConstraintTest {

  private Solution solution;
  private static Site culturalSite, naturalSite, mixedSite;
  private LessThanXSiteTypeDifferenceConstraint constraint;

  @BeforeEach
  public void setUp() {
    solution = mock(Solution.class);
    culturalSite = Site.builder().type(SiteType.Cultural).build();
    naturalSite = Site.builder().type(SiteType.Natural).build();
    mixedSite = Site.builder().type(SiteType.Mixed).build();
  }

}
