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

  // Generic parameters
  private static final String UNESCO_FILE_PATH = "src/main/resources/whc-sites-2021.xls";
  private static final String TRAVEL_MATRIX_PATH = "src/main/resources/matrix.csv";
  private static final long THREE_WEEKS_IN_SECONDS = 3 * 7 * 24 * 3600;

  // User parameters
  private static final Coordinates USER_COORDINATES = new Coordinates(0, 0);
  private static final double DESTROYER_PERCENTAGE = 0.10;
  private static final long BUDGET_TIME_IN_SECONDS = 10;
  private static final int ALGORITHM_ITERATIONS = 100;
  // Constraint parameters
  private static final int MAX_SITE_DIFFERENCE = 1;
  // Objective weights
  private static final long WEIGHT_VISITED_SITES = 1000;
  private static final long WEIGHT_VISITED_COUNTRIES = 2000;
  private static final long WEIGHT_VISITED_ENDANGERED_SITES = 3000;
  private static final long WEIGHT_TRIP_REMAINING_TIME = 1;
  private static final long WEIGHT_SITE_PARITY = 1500;

  public static void main(final String[] args) throws IOException {
    final Instance instance = createInstance();
    final ConstraintManager constraintManager = createConstraintManager();
    final ObjectiveManager objectiveManager = createObjectiveManager();
    final Destroyer destroyer = createDestroyer();
    final Repairer repairer = createRepairer();
    final Algorithm algorithm = createAlgorithm(destroyer, repairer, Stop.ITERATIONS);
    final Solver solver =
        Solver.builder()
            .constraintManager(constraintManager)
            .objectiveManager(objectiveManager)
            .algorithm(algorithm)
            .initialRepairer(repairer)
            .instance(instance)
            .build();
    final Solution solution = solver.solve();
    System.out.println(solution);
  }

  private static Instance createInstance() throws IOException {
    final var sites = new SiteReader().createSites(UNESCO_FILE_PATH);
    final var start = TravelStartLocation.builder().coordinates(USER_COORDINATES).build();
    final var matrix = new TravelMatrix(sites, TRAVEL_MATRIX_PATH, start);
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

  private static ObjectiveManager createObjectiveManager() {
    return ObjectiveManager.builder()
        .objective(
            WeightedSumObjective.builder()
                .sense(ObjectiveSense.MAXIMIZE)
                .objective(new NumberOfVisitedSitesObjective(), WEIGHT_VISITED_SITES)
                .objective(new NumberOfVisitedCountriesObjective(), WEIGHT_VISITED_COUNTRIES)
                .objective(
                    new NumberOfVisitedEndangeredSitesObjective(), WEIGHT_VISITED_ENDANGERED_SITES)
                .objective(
                    new TripRemainingTimeObjective(THREE_WEEKS_IN_SECONDS),
                    WEIGHT_TRIP_REMAINING_TIME)
                .objective(new SiteTypeParityObjective(), WEIGHT_SITE_PARITY)
                .build())
        .build();
  }

  private static Repairer createRepairer() {
    return BestVisitNewSitesRepairer.builder()
        .filter(new UnderRepresentedSiteTypeFilter())
        .stoppingCriterion(new NumberOfIterationsStoppingCriterion(75))
        .build();
  }

  private static Destroyer createDestroyer() {
    return RandomSiteDestroyer.builder()
        .percentage(DESTROYER_PERCENTAGE)
        .filter(new OverRepresentedSiteTypeFilter())
        .selector(new RandomSiteSelector(new RandomNumberGenerator(0)))
        .build();
  }

  private static Algorithm createAlgorithm(
      final Destroyer destroyer, final Repairer repairer, final Stop stop) {
    final var stoppingCriterion =
        stop.equals(Stop.TIME)
            ? new TimeBudgetStoppingCriterion(BUDGET_TIME_IN_SECONDS * 1000)
            : new NumberOfIterationsStoppingCriterion(ALGORITHM_ITERATIONS);
    return LNS.builder()
        .destroyer(destroyer)
        .repairer(repairer)
        .acceptanceCriterion(new AcceptBestSolution())
        .stoppingCriterion(stoppingCriterion)
        .build();
  }

  private enum Stop {
    TIME,
    ITERATIONS
  }
}
