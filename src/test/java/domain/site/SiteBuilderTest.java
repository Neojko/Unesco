package domain.site;

import domain.matrix.SiteBuilder;
import lombok.var;
import org.junit.jupiter.api.Test;

public class SiteBuilderTest {

  @Test
  public void process_test() {
    final SiteBuilder reader = new SiteBuilder();
    final var sites = reader.createSites("src/test/resources/whc-sites-2021-small.xls");
  }

}
