package domain.site;

import static domain.site.SiteType.Cultural;
import static domain.site.SiteType.Mixed;
import static domain.site.SiteType.Natural;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SiteTypeTest {

  @Test
  public void is_cultural_returns_true_for_cultural_site() {
    assertTrue(Cultural.isCultural());
  }

  @Test
  public void is_cultural_returns_false_for_natural_site() {
    assertFalse(Natural.isCultural());
  }

  @Test
  public void is_cultural_returns_true_for_mixed_site() {
    assertTrue(Mixed.isCultural());
  }

  @Test
  public void is_natural_returns_false_for_cultural_site() {
    assertFalse(Cultural.isNatural());
  }

  @Test
  public void is_natural_returns_true_for_natural_site() {
    assertTrue(Natural.isNatural());
  }

  @Test
  public void is_natural_returns_true_for_mixed_site() {
    assertTrue(Mixed.isNatural());
  }
}
