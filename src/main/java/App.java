import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.SiteReader;
import domain.matrix.TravelMatrix;
import java.io.IOException;
import lombok.var;

public class App {

  private static final String UNESCO_FILE_PATH = "src/main/resources/whc-sites-2021.xls";
  private static final String TRAVEL_MATRIX_PATH = "src/main/resources/matrix.csv";
  private static final Coordinates USER_COORDINATES = new Coordinates(0, 0);

  public static void main(final String[] args) throws IOException {
    final var sites = new SiteReader().createSites(UNESCO_FILE_PATH);
    final var start = TravelStartLocation.builder().coordinates(USER_COORDINATES).build();
    final var matrix = new TravelMatrix(sites, start);
    matrix.exportToCSV(TRAVEL_MATRIX_PATH);
  }

}
