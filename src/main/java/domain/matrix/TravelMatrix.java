package domain.matrix;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import domain.matrix.computers.HaversineComputer;
import domain.matrix.computers.TravelTimeComputer;
import domain.site.Site;
import domain.site.SiteNumber;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.var;

@EqualsAndHashCode
public class TravelMatrix {

  private final Map<SiteNumber, Integer> indices;
  private final long[][] travelTimeInSeconds;

  /** Creates TravelMatrix by computing time to travel Haversine distance for all pairs of sites */
  public TravelMatrix(final List<Site> sites) {
    indices = initIndices(sites);
    travelTimeInSeconds = new long[sites.size()][sites.size()];
    for (int i = 0; i < sites.size() - 1; i++) {
      final var firstSiteIndex = indices.get(sites.get(i).getUniqueNumber());
      travelTimeInSeconds[firstSiteIndex][firstSiteIndex] = 0;
      for (int j = i + 1; j < sites.size(); j++) {
        final var secondSiteIndex = indices.get(sites.get(j).getUniqueNumber());
        final var distance =
            HaversineComputer.getDistance(
                sites.get(i).getCoordinates(), sites.get(j).getCoordinates());
        final var time = TravelTimeComputer.convertToTime(distance);
        travelTimeInSeconds[firstSiteIndex][secondSiteIndex] = time;
        travelTimeInSeconds[secondSiteIndex][firstSiteIndex] = time;
      }
    }
  }

  /**
   * Creates TravelMatrix from a list of sites and a csv file containing times between pairs
   *
   * @param sites: contains all sites whose numbers belong to csv file
   * @param inputCSVFileName: contains time for all pairs of site numbers with time(i,j) = time(j,i)
   * @throws IOException: file not found
   */
  public TravelMatrix(final List<Site> sites, final String inputCSVFileName) throws IOException {
    indices = initIndices(sites);
    travelTimeInSeconds = new long[sites.size()][sites.size()];
    final var file = Files.newInputStream(Paths.get(inputCSVFileName));
    final var fileReader = new InputStreamReader(file, StandardCharsets.UTF_8);
    final CSVReader csvReader = new CSVReader(fileReader);
    String[] row = csvReader.readNext();
    while (row != null) {
      fillMatrixEntries(row);
      row = csvReader.readNext();
    }
  }

  /** @return travel time in seconds between first and second Site */
  public long time(final Site first, final Site second) {
    return time(first.getUniqueNumber(), second.getUniqueNumber());
  }

  /**
   * Exports matrix in CSV file containing a list of triplets (SiteNumber, SiteNumber, travel time)
   */
  public void exportToCSV(final String filename) throws IOException {
    final List<String[]> data = new ArrayList<>();
    final var siteNumbers = new ArrayList<>(indices.keySet());
    for (int i = 0; i < siteNumbers.size() - 1; i++) {
      for (int j = i + 1; j < siteNumbers.size(); j++) {
        data.add(createCSVEntry(siteNumbers.get(i), siteNumbers.get(j)));
      }
    }
    write(data, filename);
  }

  private Map<SiteNumber, Integer> initIndices(final List<Site> sites) {
    final var numbers = sites.stream().map(Site::getUniqueNumber).collect(Collectors.toList());
    return IntStream.range(0, numbers.size())
        .boxed()
        .collect(Collectors.toMap(numbers::get, i -> i));
  }

  /**
   * Fill the matrix with the row content when creating a new matrix
   *
   * @param row: row from CSV input file with format (SiteNumber, SiteNumber, travel time)
   */
  private void fillMatrixEntries(final String[] row) {
    final var firstSiteNumber = new SiteNumber(Integer.parseInt(row[0]));
    final var secondSiteNumber = new SiteNumber(Integer.parseInt(row[1]));
    final var travelTime = Long.parseLong(row[2]);
    travelTimeInSeconds[indices.get(firstSiteNumber)][indices.get(secondSiteNumber)] = travelTime;
    travelTimeInSeconds[indices.get(secondSiteNumber)][indices.get(firstSiteNumber)] = travelTime;
  }

  /** @return travel time in seconds between first and second SiteNumber */
  private long time(final SiteNumber first, final SiteNumber second) {
    final var firstIndex = indices.get(first);
    final var secondIndex = indices.get(second);
    return travelTimeInSeconds[firstIndex][secondIndex];
  }

  /**
   * Creates triplet with two SiteNumber objects and their travel time when exporting matrix to CSV
   */
  private String[] createCSVEntry(final SiteNumber first, final SiteNumber second) {
    return new String[] {
      String.valueOf(first.getValue()),
      String.valueOf(second.getValue()),
      String.valueOf(time(first, second))
    };
  }

  /**
   * Writes a CSV file containing the processed matrix object
   *
   * @param data: list of triplets (SiteNumber, SiteNumber, travel time)
   */
  private void write(final List<String[]> data, final String filename) throws IOException {
    final CSVWriter writer =
        new CSVWriter(
            new OutputStreamWriter(
                Files.newOutputStream(Paths.get(filename)), StandardCharsets.UTF_8));
    writer.writeAll(data);
    writer.close();
  }
}
