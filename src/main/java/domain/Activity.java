package domain;

import domain.site.Site;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author XXX
 */
@AllArgsConstructor
@Getter
public class Activity {

  public final static TemporalAmount NUMBER_OF_HOURS_PER_SITE = Duration.ofHours(6);

  private final Site site;
  private final Instant earliestArrivalTime;
  private final Instant latestArrivalTime;

}
