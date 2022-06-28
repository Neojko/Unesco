package domain.site;

import com.google.errorprone.annotations.Immutable;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Immutable
public class SiteStatus {

  private final boolean isCultural;
  private final boolean isNatural;
  private final boolean isEndangered;

}
