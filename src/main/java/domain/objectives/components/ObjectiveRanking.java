package domain.objectives.components;

import domain.objectives.Objective;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * Contains list of objectives in lexicographical order
 */

@Builder
@Getter
public class ObjectiveRanking {

  @Singular private final List<Objective> objectives;

}
