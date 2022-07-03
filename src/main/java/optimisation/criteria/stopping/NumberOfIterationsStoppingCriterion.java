package optimisation.criteria.stopping;

import lombok.Getter;

public class NumberOfIterationsStoppingCriterion implements StoppingCriterion {

  @Getter private final int maxNumberOfIterations;
  private int currentNumberOfIterations;

  public NumberOfIterationsStoppingCriterion(final int maxNumberOfIterations) {
    this.maxNumberOfIterations = maxNumberOfIterations;
  }

  @Override
  public void initialise() {
    currentNumberOfIterations = 0;
  }

  @Override
  public void update() {
    currentNumberOfIterations++;
  }

  @Override
  public boolean isMet() {
    return currentNumberOfIterations >= maxNumberOfIterations;
  }
}
