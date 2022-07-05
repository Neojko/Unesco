package app;

import lombok.Builder;
import lombok.Getter;

/** Quick and dirty config to give when running a .jar (no time to do better) */
@Builder
@Getter
public class AppConfig {

  private final String sitesFilePath;
  private final String matrixFilePath;
  private final double latitude;
  private final double longitude;
  private final long timeBudgetInSeconds;
  private final double destroyerRemovalPercentage;
  private long weightNumberOfVisitedSitesObjective;
  private long weightNumberOfVisitedCountriesObjective;
  private long weightNumberOfVisitedEndangeredSitesObjective;
  private long weightSiteTypeParityObjective;
  private long weightRemainingTripDurationObjective;
}
