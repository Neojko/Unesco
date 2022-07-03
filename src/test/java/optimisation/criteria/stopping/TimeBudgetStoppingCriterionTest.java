package optimisation.criteria.stopping;

import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeBudgetStoppingCriterionTest {

  @Test
  public void test_is_met_condition() {
    final var criterion = new TimeBudgetStoppingCriterion(500L);
    criterion.initialise();
    Assertions.assertFalse(criterion.isMet());
    try {
      Thread.sleep(500);
    } catch (final InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    Assertions.assertTrue(criterion.isMet());
  }
}
