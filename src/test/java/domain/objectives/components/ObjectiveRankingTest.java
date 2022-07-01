package domain.objectives.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import domain.objectives.Objective;
import domain.objectives.components.ObjectiveRanking.ObjectiveRankingBuilder;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectiveRankingTest {

  private Objective objective1, objective2;
  private ObjectiveRanking objectiveRanking;

  @BeforeEach
  public void setUp() {
    objective1 = mock(Objective.class);
    objective2 = mock(Objective.class);
    objectiveRanking =
        new ObjectiveRankingBuilder().objective(objective1).objective(objective2).build();
  }

  @Test
  public void test_constructor() {
    final var objectivesList = objectiveRanking.getObjectives();
    assertEquals(2, objectivesList.size());
    assertEquals(objective1, objectiveRanking.getObjectives().get(0));
    assertEquals(objective2, objectiveRanking.getObjectives().get(1));
  }
}
