package domain.matrix;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import domain.locations.Location;
import domain.locations.LocationID;
import domain.locations.TravelStartLocation;
import domain.locations.sites.Site;
import domain.matrix.computers.HaversineComputer;
import domain.matrix.computers.TravelTimeComputer;
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

  private final Map<LocationID, Integer> indices;
  private final long[][] travelTimeInSeconds;

  /** Creates TravelMatrix by computing time to travel Haversine distance for all pairs of locations */
  public TravelMatrix(final List<Location> locations) {
    indices = initIndices(locations);
    travelTimeInSeconds = new long[locations.size()][locations.size()];
    for (int i = 0; i < locations.size() - 1; i++) {
      computeMatrixEntries(locations, locations.get(i));
    }
  }

  /** Creates TravelMatrix by computing time to travel Haversine distance for all pairs of locations */
  public TravelMatrix(final List<Site> sites, final TravelStartLocation start) {
    final List<Location> locations = new ArrayList<>(sites);
    locations.add(start);
    indices = initIndices(locations);
    travelTimeInSeconds = new long[locations.size()][locations.size()];
    for (int i = 0; i < locations.size() - 1; i++) {
      computeMatrixEntries(locations, locations.get(i));
    }
  }

  /**
   * Creates matrix with entries that are already computed apart from the travelStartLocation ones
   * @param sites: contains all sites whose locationIDs belong to csv file
   * @param inputCSVFileName: contains time for all pairs of locationIDs with time(i,j) = time(j,i)
   * @param travelStartLocation: new Location to add in the matrix
   * @throws IOException: file not found
   */
  public TravelMatrix(
      final List<Site> sites,
      final String inputCSVFileName,
      final TravelStartLocation travelStartLocation
  ) throws IOException {
    final List<Location> locations = new ArrayList<>(sites);
    locations.add(travelStartLocation);
    indices = initIndices(locations);
    travelTimeInSeconds = new long[locations.size()][locations.size()];
    final var file = Files.newInputStream(Paths.get(inputCSVFileName));
    final var fileReader = new InputStreamReader(file, StandardCharsets.UTF_8);
    final CSVReader csvReader = new CSVReader(fileReader);
    String[] row = csvReader.readNext();
    while (row != null) {
      fillMatrixEntries(row);
      row = csvReader.readNext();
    }
    computeMatrixEntries(locations, travelStartLocation);
  }

  /** @return travel time in seconds between first and second Location */
  public long time(final Location first, final Location second) {
    return time(first.getLocationID(), second.getLocationID());
  }

  /**
   * Exports matrix in CSV file containing a list of triplets (LocationID, LocationID, travel time)
   */
  public void exportToCSV(final String filename) throws IOException {
    final List<String[]> data = new ArrayList<>();
    final var locationIDs = new ArrayList<>(indices.keySet());
    for (int i = 0; i < locationIDs.size() - 1; i++) {
      for (int j = i + 1; j < locationIDs.size(); j++) {
        data.add(createCSVEntry(locationIDs.get(i), locationIDs.get(j)));
      }
    }
    write(data, filename);
  }

  private Map<LocationID, Integer> initIndices(final List<Location> locations) {
    final var locationIDs = locations.stream()
        .map(Location::getLocationID).collect(Collectors.toList());
    return IntStream.range(0, locationIDs.size())
        .boxed()
        .collect(Collectors.toMap(locationIDs::get, i -> i));
  }

  /**
   * Fill the matrix with the row content when creating a new matrix
   *
   * @param row: row from CSV input file with format (LocationID, LocationID, travel time)
   */
  private void fillMatrixEntries(final String[] row) {
    final var firstLocationID = new LocationID(Integer.parseInt(row[0]));
    final var secondLocationID = new LocationID(Integer.parseInt(row[1]));
    final var travelTime = Long.parseLong(row[2]);
    travelTimeInSeconds[indices.get(firstLocationID)][indices.get(secondLocationID)] = travelTime;
    travelTimeInSeconds[indices.get(secondLocationID)][indices.get(firstLocationID)] = travelTime;
  }

  /**
   * Computes and adds all entries related to location in the matrix
   */
  private void computeMatrixEntries(final List<Location> locations, final Location location) {
    final var locationIndex = indices.get(location.getLocationID());
    travelTimeInSeconds[locationIndex][locationIndex] = 0;
    for (final var otherLocation : locations) {
      final var otherLocationIndex = indices.get(otherLocation.getLocationID());
      final var distance =
          HaversineComputer.getDistance(location.getCoordinates(), otherLocation.getCoordinates());
      final var time = TravelTimeComputer.convertToTime(distance);
      travelTimeInSeconds[locationIndex][otherLocationIndex] = time;
      travelTimeInSeconds[otherLocationIndex][locationIndex] = time;
    }
  }

  /** @return travel time in seconds between first and second LocationID */
  private long time(final LocationID first, final LocationID second) {
    final var firstIndex = indices.get(first);
    final var secondIndex = indices.get(second);
    return travelTimeInSeconds[firstIndex][secondIndex];
  }

  /**
   * Creates triplet with two LocationID objects and their travel time when exporting matrix to CSV
   */
  private String[] createCSVEntry(final LocationID first, final LocationID second) {
    return new String[] {
      String.valueOf(first.getValue()),
      String.valueOf(second.getValue()),
      String.valueOf(time(first, second))
    };
  }

  /**
   * Writes a CSV file containing the processed matrix object
   *
   * @param data: list of triplets (LocationID, LocationID, travel time)
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
