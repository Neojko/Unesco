package domain.site;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import lombok.var;
import org.junit.jupiter.api.Test;

public class SiteReaderTest {

  @Test
  public void create_sites_test() {
    final SiteReader reader = new SiteReader();
    final var sites = reader.createSites("src/test/resources/whc-sites-2021-small.xls");
    final var first =
        Site.builder()
            .name("Cultural Landscape and Archaeological Remains of the Bamiyan Valley")
            .uniqueNumber(230)
            .coordinates(new Coordinates(34.84694, 67.82525))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Afghanistan"))))
            .type(SiteType.Cultural)
            .isEndangered()
            .build();
    final var second =
        Site.builder()
            .name("Tassili n'Ajjer")
            .uniqueNumber(198)
            .coordinates(new Coordinates(25.5, 9))
            .countries(new ArrayList<>(Collections.singletonList(new Country("Algeria"))))
            .type(SiteType.Mixed)
            .build();
    final var third =
        Site.builder()
            .name("Mosi-oa-Tunya / Victoria Falls")
            .uniqueNumber(593)
            .coordinates(new Coordinates(-17.92453, 25.85539))
            .countries(
                new ArrayList<>(Arrays.asList(new Country("Zambia"), new Country("Zimbabwe"))))
            .type(SiteType.Natural)
            .build();
    assertEquals(sites.get(0), first);
    assertEquals(sites.get(1), second);
    assertEquals(sites.get(2), third);
  }
}
