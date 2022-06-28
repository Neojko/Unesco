package domain.site;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.var;
import org.junit.jupiter.api.Test;

public class SiteStatusTest {

  @Test
  public void is_cultural_returns_true() {
    final var status = SiteStatus.builder().isCultural(true).build();
    assertTrue(status.isCultural());
  }

  @Test
  public void is_cultural_returns_false() {
    final var status = SiteStatus.builder().build();
    assertFalse(status.isCultural());
  }

  @Test
  public void is_natural_returns_true() {
    final var status = SiteStatus.builder().isNatural(true).build();
    assertTrue(status.isNatural());
  }

  @Test
  public void is_natural_returns_false() {
    final var status = SiteStatus.builder().build();
    assertFalse(status.isNatural());
  }

  @Test
  public void is_endangered_returns_true() {
    final var status = SiteStatus.builder().isEndangered(true).build();
    assertTrue(status.isEndangered());
  }

  @Test
  public void is_endangered_returns_false() {
    final var status = SiteStatus.builder().build();
    assertFalse(status.isEndangered());
  }

}
