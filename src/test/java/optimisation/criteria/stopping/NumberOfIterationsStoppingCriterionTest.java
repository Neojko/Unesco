package optimisation.criteria.stopping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class NumberOfIterationsStoppingCriterionTest {

  private NumberOfIterationsStoppingCriterion criterion;

  @BeforeEach
  void setup() {
    criterion = new NumberOfIterationsStoppingCriterion(2);
  }

  @ParameterizedTest(name = "number of updates = {0}, expectedResult = {1}")
  @CsvSource({"0, false", "1, false", "2, true", "3, true"})
  void test_is_met(final int numberOfUpdates, final boolean expectedResult) {
    criterion.initialise();
    for (int i = 0; i < numberOfUpdates; i++) {
      criterion.update();
    }
    assertEquals(expectedResult, criterion.isMet());
  }
}
