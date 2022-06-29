package domain;

import domain.matrix.TravelMatrix;
import java.util.List;
import lombok.Getter;

@Getter
public class Solution {

  private final TravelMatrix matrix;
  private final List<Activity> activities;
  private final boolean isFeasible;

  public Solution(final TravelMatrix matrix, final List<Activity> activities) {
    this.matrix = matrix;
    this.activities = activities;
    this.isFeasible = true; // TODO update with ConstraintManager
  }
}
