package app;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Class to read the quick and dirty AppConfig */
public class JsonAppConfig {

  @JsonProperty("sites_file_path")
  private String sitesFilePath;

  @JsonProperty("matrix_file_path")
  private String matrixFilePath;

  @JsonProperty() private double latitude;

  @JsonProperty() private double longitude;

  @JsonProperty("time_budget_in_seconds")
  private long timeBudgetInSeconds;

  @JsonProperty("destroyer_removal_percentage")
  private double destroyerRemovalPercentage;

  @JsonProperty("weight_number_of_visited_sites_objective")
  private long weightNumberOfVisitedSitesObjective;

  @JsonProperty("weight_number_of_visited_countries_objective")
  private long weightNumberOfVisitedCountriesObjective;

  @JsonProperty("weight_number_of_visited_endangered_sites_objective")
  private long weightNumberOfVisitedEndangeredSitesObjective;

  @JsonProperty("weight_site_type_parity_objective")
  private long weightSiteTypeParityObjective;

  @JsonProperty("weight_remaining_trip_duration_objective")
  private long weightRemainingTripDurationObjective;

  public AppConfig toConfig() {
    return AppConfig.builder()
        .sitesFilePath(sitesFilePath)
        .matrixFilePath(matrixFilePath)
        .latitude(latitude)
        .longitude(longitude)
        .timeBudgetInSeconds(timeBudgetInSeconds)
        .destroyerRemovalPercentage(destroyerRemovalPercentage)
        .weightNumberOfVisitedSitesObjective(weightNumberOfVisitedSitesObjective)
        .weightNumberOfVisitedCountriesObjective(weightNumberOfVisitedCountriesObjective)
        .weightNumberOfVisitedEndangeredSitesObjective(
            weightNumberOfVisitedEndangeredSitesObjective)
        .weightSiteTypeParityObjective(weightSiteTypeParityObjective)
        .weightRemainingTripDurationObjective(weightRemainingTripDurationObjective)
        .build();
  }
}
