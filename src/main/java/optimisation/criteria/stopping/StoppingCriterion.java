package optimisation.criteria.stopping;

public interface StoppingCriterion {

  void initialise();

  void update();

  boolean isMet();

}
