package app;

import domain.Instance;
import domain.constraints.ConstraintManager;
import domain.constraints.LessThanXSiteTypeDifferenceConstraint;
import domain.constraints.MaxTripDurationConstraint;
import domain.locations.Coordinates;
import domain.locations.TravelStartLocation;
import domain.locations.sites.SiteReader;
import domain.matrix.TravelMatrix;
import domain.objectives.NumberOfVisitedCountriesObjective;
import domain.objectives.NumberOfVisitedEndangeredSitesObjective;
import domain.objectives.NumberOfVisitedSitesObjective;
import domain.objectives.ObjectiveManager;
import domain.objectives.SiteTypeParityObjective;
import domain.objectives.TripRemainingTimeObjective;
import domain.objectives.components.ObjectiveSense;
import domain.objectives.components.WeightedSumObjective;
import domain.solution.Solution;
import java.io.IOException;
import lombok.var;
import optimisation.Solver;
import optimisation.algorithms.Algorithm;
import optimisation.algorithms.LNS;
import optimisation.choosers.filters.OverRepresentedSiteTypeFilter;
import optimisation.choosers.filters.UnderRepresentedSiteTypeFilter;
import optimisation.choosers.selectors.RandomSiteSelector;
import optimisation.criteria.acceptance.AcceptBestSolution;
import optimisation.criteria.stopping.NumberOfIterationsStoppingCriterion;
import optimisation.criteria.stopping.TimeBudgetStoppingCriterion;
import optimisation.modificators.destroyers.Destroyer;
import optimisation.modificators.destroyers.RandomSiteDestroyer;
import optimisation.modificators.repairers.BestVisitNewSitesRepairer;
import optimisation.modificators.repairers.Repairer;
import util.RandomNumberGenerator;

public class App {

  // Fixed parameters
  private static final long THREE_WEEKS_IN_SECONDS = 3 * 7 * 24 * 3600;
  private static final int REPAIRER_ITERATIONS = 80;
  private static final int MAX_SITE_DIFFERENCE = 1;

  public static void main(final String[] args) throws IOException {
    final String jsonAppConfigPath = args[0];
    System.out.println("Reading config from " + jsonAppConfigPath);
    final AppConfig config = JsonHelper.build(jsonAppConfigPath, JsonAppConfig.class).toConfig();
    final Instance instance = createInstance(config);
    final ConstraintManager constraintManager = createConstraintManager();
    final ObjectiveManager objectiveManager = createObjectiveManager(config);
    final Destroyer destroyer = createDestroyer(config);
    final Repairer repairer = createRepairer();
    final Algorithm algorithm = createAlgorithm(config, destroyer, repairer);
    final Solver solver =
        Solver.builder()
            .constraintManager(constraintManager)
            .objectiveManager(objectiveManager)
            .algorithm(algorithm)
            .initialRepairer(repairer)
            .instance(instance)
            .build();
    System.out.println("Running solver for " + config.getTimeBudgetInSeconds() + " seconds..");
    final Solution solution = solver.solve();
    System.out.println(solution);
  }

  private static Instance createInstance(final AppConfig config) throws IOException {
    final var sites = new SiteReader().createSites(config.getSitesFilePath());
    final var userCoordinates = new Coordinates(config.getLatitude(), config.getLongitude());
    final var start = TravelStartLocation.builder().coordinates(userCoordinates).build();
    final var matrix = new TravelMatrix(sites, config.getMatrixFilePath(), start);
    return Instance.builder().start(start).sites(sites).matrix(matrix).build();
  }

  private static ConstraintManager createConstraintManager() {
    return ConstraintManager.builder()
        .constraint(new MaxTripDurationConstraint(THREE_WEEKS_IN_SECONDS))
        .constraint(
            LessThanXSiteTypeDifferenceConstraint.builder()
                .maxDifference(MAX_SITE_DIFFERENCE)
                .build())
        .build();
  }

  private static ObjectiveManager createObjectiveManager(final AppConfig config) {
    return ObjectiveManager.builder()
        .objective(
            WeightedSumObjective.builder()
                .sense(ObjectiveSense.MAXIMIZE)
                .objective(
                    new NumberOfVisitedSitesObjective(),
                    config.getWeightNumberOfVisitedSitesObjective())
                .objective(
                    new NumberOfVisitedCountriesObjective(),
                    config.getWeightNumberOfVisitedCountriesObjective())
                .objective(
                    new NumberOfVisitedEndangeredSitesObjective(),
                    config.getWeightNumberOfVisitedEndangeredSitesObjective())
                .objective(new SiteTypeParityObjective(), config.getWeightSiteTypeParityObjective())
                .objective(
                    new TripRemainingTimeObjective(THREE_WEEKS_IN_SECONDS),
                    config.getWeightRemainingTripDurationObjective())
                .build())
        .build();
  }

  private static Repairer createRepairer() {
    return BestVisitNewSitesRepairer.builder()
        .filter(new UnderRepresentedSiteTypeFilter())
        .stoppingCriterion(new NumberOfIterationsStoppingCriterion(REPAIRER_ITERATIONS))
        .build();
  }

  private static Destroyer createDestroyer(final AppConfig config) {
    return RandomSiteDestroyer.builder()
        .percentage(config.getDestroyerRemovalPercentage())
        .filter(new OverRepresentedSiteTypeFilter())
        .selector(new RandomSiteSelector(new RandomNumberGenerator(0)))
        .build();
  }

  private static Algorithm createAlgorithm(
      final AppConfig config, final Destroyer destroyer, final Repairer repairer) {
    final var stoppingCriterion =
        new TimeBudgetStoppingCriterion(config.getTimeBudgetInSeconds() * 1000);
    return LNS.builder()
        .destroyer(destroyer)
        .repairer(repairer)
        .acceptanceCriterion(new AcceptBestSolution())
        .stoppingCriterion(stoppingCriterion)
        .build();
  }
}
