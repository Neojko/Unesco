package domain.objectives.components;

import java.util.Comparator;
import lombok.var;

/**
 * This class aims to compare two ObjectiveValues objects relatively to:
 * - 1) Their site type parity score
 * - 2) The weighted sum in the assessment, which is:
 *    - i) 1 * Number of visited sites
 *    - ii) 2 * Number of distinct countries
 *    - iii) 3 * Number of endangered visited sites
 */

public class ObjectiveValuesComparator implements Comparator<ObjectiveValues> {

  public static int WEIGHT_NUMBER_OF_VISITED_SITES = 1;
  public static int WEIGHT_NUMBER_OF_VISITED_COUNTRIES = 2;
  public static int WEIGHT_NUMBER_OF_ENDANGERED_VISITED_SITES = 3;

  /**
   * Compares ObjectivesValues objects relatively to the problem definition, i.e., we first want
   * to optimise the number site type parity and then do a weighted sum on the remaining objectives
   * Returns -1 if first is better than second, 0 if equality and 1 if second is better than first
   */
  @Override
  public int compare(final ObjectiveValues first, final ObjectiveValues second) {
    final int parityComparison = compareParity(first, second);
    if (parityComparison != 0) {
      return parityComparison;
    }
    return compareWeightedSum(first, second);
  }

  /**
   * Compares ObjectivesValues objects relatively to their value for SityTypeParityObjective
   * Returns -1 if first is better than second, 0 if equality and 1 if second is better than first
   * PRE: first and second both have values for the given objective.
   */
  private int compareParity(final ObjectiveValues first, final ObjectiveValues second) {
    final var valueFirst = first.getValues().get(ObjectiveName.SITE_TYPE_PARITY);
    final var valueSecond = second.getValues().get(ObjectiveName.SITE_TYPE_PARITY);
    return valueFirst.compareTo(valueSecond);
  }

  private int compareWeightedSum(final ObjectiveValues first, final ObjectiveValues second) {
    final var valueFirst = getWeightedSumObjectiveValue(first);
    final var valueSecond = getWeightedSumObjectiveValue(second);
    return valueFirst.compareTo(valueSecond);
  }

  private ObjectiveValue getWeightedSumObjectiveValue(final ObjectiveValues objectiveValues) {
    final var values = objectiveValues.getValues();
    final var value = values.get(ObjectiveName.NUMBER_OF_VISITED_SITES).getValue() * WEIGHT_NUMBER_OF_VISITED_SITES
        + values.get(ObjectiveName.NUMBER_OF_VISITED_COUNTRIES).getValue() * WEIGHT_NUMBER_OF_VISITED_COUNTRIES
        + values.get(ObjectiveName.NUMBER_OF_ENDANGERED_VISITED_SITES) .getValue()
        * WEIGHT_NUMBER_OF_ENDANGERED_VISITED_SITES;
    return ObjectiveValue.builder().sense(ObjectiveSense.MAXIMIZE).value(value).build();
  }
}
