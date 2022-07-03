package optimisation.criteria.stopping;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

@Builder
@Getter
public class StoppingCriteria implements StoppingCriterion {

  @Singular private final List<StoppingCriterion> stoppingCriterions;

  @Override
  public void initialise() {
    stoppingCriterions.forEach(StoppingCriterion::initialise);
  }

  @Override
  public void update() {
    stoppingCriterions.forEach(StoppingCriterion::update);
  }

  // Returns true if any of the stopping criterion is met.
  @Override
  public boolean isMet() {
    return stoppingCriterions.stream().anyMatch(StoppingCriterion::isMet);
  }

}
