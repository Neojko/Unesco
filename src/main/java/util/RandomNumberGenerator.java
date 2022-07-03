package util;

import java.util.Random;

public class RandomNumberGenerator {

  private final Random random;

  public RandomNumberGenerator(final int seed) {
    random = new Random(seed);
  }

  public int nextInt(final int bound) {
    return random.nextInt(bound);
  }

}
