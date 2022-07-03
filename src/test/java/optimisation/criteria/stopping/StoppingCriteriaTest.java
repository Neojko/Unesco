package optimisation.criteria.stopping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class StoppingCriteriaTest {

  private StoppingCriterion stoppingCriterion1;
  private StoppingCriterion stoppingCriterion2;
  private StoppingCriterion stoppingCriterion3;
  private StoppingCriteria stoppingCriteria;

  @BeforeEach
  public void setUp() {
    stoppingCriterion1 = mock(StoppingCriterion.class);
    stoppingCriterion2 = mock(StoppingCriterion.class);
    stoppingCriterion3 = mock(StoppingCriterion.class);

    stoppingCriteria =
        StoppingCriteria.builder()
            .stoppingCriterion(stoppingCriterion1)
            .stoppingCriterion(stoppingCriterion2)
            .stoppingCriterion(stoppingCriterion3)
            .build();
  }

  @Test
  public void test_initialise() {
    stoppingCriteria.initialise();

    verify(stoppingCriterion1, times(1)).initialise();
    verify(stoppingCriterion2, times(1)).initialise();
    verify(stoppingCriterion3, times(1)).initialise();
  }

  @Test
  public void test_update() {
    stoppingCriteria.update();
    stoppingCriteria.update();

    verify(stoppingCriterion1, times(2)).update();
    verify(stoppingCriterion2, times(2)).update();
    verify(stoppingCriterion3, times(2)).update();
  }

  @ParameterizedTest(name = "isStoppingCriteria3Met = {0}, expectedResult = {1}")
  @CsvSource({"false, false", "true, true"})
  public void test_is_met(final boolean isCriteria3Met, final boolean expectedResult) {
    when(stoppingCriterion1.isMet()).thenReturn(false);
    when(stoppingCriterion2.isMet()).thenReturn(false);
    when(stoppingCriterion3.isMet()).thenReturn(isCriteria3Met);

    final var result = stoppingCriteria.isMet();

    assertEquals(expectedResult, result);
    verify(stoppingCriterion1, times(1)).isMet();
    verify(stoppingCriterion2, times(1)).isMet();
    verify(stoppingCriterion3, times(1)).isMet();
  }
}
