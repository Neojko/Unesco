package domain.objectives.components;

import com.google.errorprone.annotations.Immutable;
import domain.objectives.interfaces.Objective;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

/** Contains list of objectives in lexicographical order */
@EqualsAndHashCode
@Immutable
@Builder
@Getter
public class ObjectiveRanking {

  @Singular private final List<Objective> objectives;
}
