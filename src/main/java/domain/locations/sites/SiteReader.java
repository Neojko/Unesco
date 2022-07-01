package domain.locations.sites;

import domain.locations.Coordinates;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is made to read Unesco .xls document, and translate it to i) a .csv file containing
 * all travel times between pairs of coordinates and ii) create a .json file containing all sites.
 */
public class SiteReader {

  private final Logger LOGGER = LoggerFactory.getLogger(SiteReader.class);

  private static final int LOCATION_ID_INDEX = 0;
  private static final int NAME_INDEX = 3;
  private static final int DANGER_INDEX = 11;
  private static final int LONGITUDE_INDEX = 14;
  private static final int LATITUDE_INDEX = 15;
  private static final int TYPE_INDEX = 28;
  private static final int COUNTRIES_INDEX = 30;

  /** Creates Site objects from .xls file in Unesco format */
  public List<Site> createSites(final String inputFileName) {
    final List<Site> sites = new ArrayList<>();
    try (InputStream inp = Files.newInputStream(Paths.get(inputFileName))) {
      final Workbook workbook = WorkbookFactory.create(inp);
      final Sheet sheet = workbook.getSheetAt(0);
      final Iterator<Row> objIterator = sheet.rowIterator();
      // Skipping header
      objIterator.next();
      while (objIterator.hasNext()) {
        final Row row = objIterator.next();
        sites.add(createSite(row));
      }
    } catch (final Exception exception) {
      LOGGER.info(exception.getMessage());
    }
    return sites;
  }

  /** Creates Site from row of .xls file in Unesco format */
  private Site createSite(final Row row) {
    return Site.builder()
        .name(row.getCell(NAME_INDEX).getStringCellValue())
        .locationID((int) row.getCell(LOCATION_ID_INDEX).getNumericCellValue())
        .coordinates(
            new Coordinates(
                row.getCell(LATITUDE_INDEX).getNumericCellValue(),
                row.getCell(LONGITUDE_INDEX).getNumericCellValue()))
        .countries(getCountries(row.getCell(COUNTRIES_INDEX).getStringCellValue()))
        .type(SiteType.valueOf(row.getCell(TYPE_INDEX).getStringCellValue()))
        .isEndangered(row.getCell(DANGER_INDEX).getNumericCellValue() == 1.0)
        .build();
  }

  private List<Country> getCountries(final String entry) {
    final String[] countryNames = entry.split(",");
    return Arrays.stream(countryNames).map(Country::new).collect(Collectors.toList());
  }
}
