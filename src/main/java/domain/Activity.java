package domain;

import domain.site.Site;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Activity {

  public static final TemporalAmount NUMBER_OF_HOURS_PER_SITE = Duration.ofHours(6);

  private final Site site;
  @Setter private Instant earliestArrivalTime;
  @Setter private Instant latestArrivalTime;
}
