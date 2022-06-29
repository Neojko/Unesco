package domain.matrix;

import domain.site.Coordinates;
import domain.site.Country;
import domain.site.Site;
import domain.site.SiteStatus;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.var;
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
public class SiteBuilder {

  private final Logger LOGGER = LoggerFactory.getLogger(SiteBuilder.class);

  private final static String CULTURAL = "Cultural";
  private final static String NATURAL = "Natural";
  private final static String MIXED = "Mixed";

  private final static int SITE_NAME_INDEX = 3;
  private final static int DANGER_INDEX = 11;
  private final static int LONGITUDE_INDEX = 14;
  private final static int LATITUDE_INDEX = 15;
  private final static int CATEGORY_INDEX = 28;
  private final static int COUNTRIES_INDEX = 30;


  /**
   * Creates Site objects from .xls file in Unesco format
   */
  public List<Site> createSites(final String inputFileName) {
    final List<Site> sites = new ArrayList<>();
    try (InputStream inp = Files.newInputStream(Paths.get(inputFileName))) {
      Workbook workbook = WorkbookFactory.create(inp);
      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> objIterator = sheet.rowIterator();
      // Skipping header
      objIterator.next();
      while(objIterator.hasNext()){
        final Row row = objIterator.next();
        sites.add(createSite(row));
      }
    } catch (Exception exception) {
      LOGGER.info(exception.getMessage());
    }
    return sites;
  }

  /**
   * Creates Site from row of .xls file in Unesco format
   */
  private Site createSite(final Row row) {
    return Site.builder()
        .name(row.getCell(SITE_NAME_INDEX).getStringCellValue())
        .coordinates(
            new Coordinates(
                row.getCell(LATITUDE_INDEX).getNumericCellValue(),
                row.getCell(LONGITUDE_INDEX).getNumericCellValue()
            )
        )
        .countries(getCountries(row.getCell(COUNTRIES_INDEX).getStringCellValue()))
        .status(
            SiteStatus.builder()
                .isCultural(isCultural(row.getCell(CATEGORY_INDEX).getStringCellValue()))
                .isNatural(isNatural(row.getCell(CATEGORY_INDEX).getStringCellValue()))
                .isEndangered(row.getCell(DANGER_INDEX).getNumericCellValue() == 1.0)
                .build()
        )
        .build();
  }

  private boolean isCultural(final String entry) {
    return entry.equals(CULTURAL) || entry.equals(MIXED);
  }

  private boolean isNatural(final String entry) {
    return entry.equals(NATURAL) || entry.equals(MIXED);
  }

  private List<Country> getCountries(final String entry) {
    final String[] countryNames = entry.split(",");
    return Arrays.stream(countryNames).map(Country::new).collect(Collectors.toList());
  }
}
