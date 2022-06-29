package domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import domain.site.Site;
import java.time.Instant;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ActivityTest {

  private Site site;
  private Instant earliestArrivalTime;
  private Instant latestArrivalTime;
  private Activity activity;

  @BeforeEach
  public void setUp() {
    site = mock(Site.class);
    earliestArrivalTime = Instant.ofEpochSecond(1);
    latestArrivalTime = Instant.ofEpochSecond(2);
    activity = new Activity(site, earliestArrivalTime, latestArrivalTime);
  }

  @Test
  public void get_site_test() {
    assertEquals(site, activity.getSite());
  }

  @Test
  public void get_earliest_arrival_time() {
    assertEquals(earliestArrivalTime, activity.getEarliestArrivalTime());
  }

  @Test
  public void get_latest_arrival_time() {
    assertEquals(latestArrivalTime, activity.getLatestArrivalTime());
  }

  @Test
  public void set_earliest_arrival_time() {
    final var newInstant = Instant.ofEpochSecond(3);
    activity.setEarliestArrivalTime(newInstant);
    assertEquals(newInstant, activity.getEarliestArrivalTime());
  }

  @Test
  public void set_latest_arrival_time() {
    final var newInstant = Instant.ofEpochSecond(3);
    activity.setLatestArrivalTime(newInstant);
    assertEquals(newInstant, activity.getLatestArrivalTime());
  }
}
