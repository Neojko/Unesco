package optimisation.criteria.stopping;

import static java.lang.System.currentTimeMillis;

import lombok.Getter;

public class TimeBudgetStoppingCriterion implements StoppingCriterion {

  @Getter
  private final long timeBudgetInMilliSeconds;
  private long startTime;

  public TimeBudgetStoppingCriterion(final long timeBudgetInMilliSeconds) {
    this.timeBudgetInMilliSeconds = timeBudgetInMilliSeconds;
  }

  @Override
  public void initialise() {
    startTime = currentTimeMillis();
  }

  @Override
  public void update() {
    // Nothing to do
  }

  @Override
  public boolean isMet() {
    return currentTimeMillis() - startTime >= timeBudgetInMilliSeconds;
  }

}
