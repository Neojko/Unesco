package domain.matrix;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * This class is made to - read Unesco .xls document, and - translate to a .csv file containing all
 * travel times between locations
 */
public class TravelMatrixCSVFileCreator {

  public void thing(final String inputFileName) {
    try (InputStream inp = Files.newInputStream(Paths.get(inputFileName))) {
      Workbook wb = WorkbookFactory.create(inp);
      Sheet sheet = wb.getSheetAt(0);
      Row row = sheet.getRow(2);
      Cell cell = row.getCell(3);
      if (cell != null) System.out.println("Data: " + cell);
      else System.out.println("Cell is empty");
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
