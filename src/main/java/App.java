import domain.Instance;
import domain.constraints.ConstraintManager;
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
import optimisation.modificators.destroyers.RandomSiteDestroyer;
import optimisation.modificators.repairers.BestVisitNewSitesRepairer;
import util.RandomNumberGenerator;

public class App {

  private static final String UNESCO_FILE_PATH = "src/main/resources/whc-sites-2021.xls";
  private static final String TRAVEL_MATRIX_PATH = "src/main/resources/matrix.csv";
  private static final Coordinates USER_COORDINATES = new Coordinates(0, 0);
  private static final long THREE_WEEKS_IN_SECONDS = 3 * 7 * 24 * 3600;

  public static void main(final String[] args) throws IOException {
    final Instance instance = createInstance();
    final Solver solver = createSolver(instance);
    final Solution solution = solver.solve();
    System.out.println(solution);
  }

  private static Instance createInstance() throws IOException {
    final var sites = new SiteReader().createSites(UNESCO_FILE_PATH);
    final var start = TravelStartLocation.builder().coordinates(USER_COORDINATES).build();
    final var matrix = new TravelMatrix(sites, TRAVEL_MATRIX_PATH, start);
    return Instance.builder().start(start).sites(sites).matrix(matrix).build();
  }

  private static Solver createSolver(final Instance instance) {
    final var constraintManager =
        ConstraintManager.builder()
            .constraint(new MaxTripDurationConstraint(THREE_WEEKS_IN_SECONDS))
            .build();
    final var objectiveManager =
        ObjectiveManager.builder()
            .objective(new SiteTypeParityObjective())
            .objective(
                WeightedSumObjective.builder()
                    .sense(ObjectiveSense.MAXIMIZE)
                    .objective(new NumberOfVisitedSitesObjective(), 1L)
                    .objective(new NumberOfVisitedCountriesObjective(), 2L)
                    .objective(new NumberOfVisitedEndangeredSitesObjective(), 3L)
                    .build())
            .build();
    final var repairer =
        BestVisitNewSitesRepairer.builder()
            .filter(new UnderRepresentedSiteTypeFilter())
            .stoppingCriterion(new NumberOfIterationsStoppingCriterion(75))
            .build();
    final var destroyer =
        RandomSiteDestroyer.builder()
            .percentage(0.10)
            .filter(new OverRepresentedSiteTypeFilter())
            .selector(new RandomSiteSelector(new RandomNumberGenerator(0)))
            .build();
    final Algorithm algorithm =
        LNS.builder()
            .destroyer(destroyer)
            .repairer(repairer)
            .acceptanceCriterion(new AcceptBestSolution())
            .stoppingCriterion(new TimeBudgetStoppingCriterion(10 * 1000))
            .build();
    return Solver.builder()
        .constraintManager(constraintManager)
        .objectiveManager(objectiveManager)
        .algorithm(algorithm)
        .initialRepairer(repairer)
        .instance(instance)
        .build();
  }
}
