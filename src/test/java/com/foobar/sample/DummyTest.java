package com.foobar.sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// The two "*_mishandled_in_surefire" tests are not handled properly by surefire,
// only for this specific name-pattern used in the Parameterized/Repeated test
// -> surefire (3.0.0-M7, at least) counts all 5 runs of these methods in union, i.e. treating them as one
// -> If using rerunFailingTestsCount, it makes surefire treat these methods (all 5 runs) as a Flake, and won't fail the build
// Even just removing the square brackets from the name-pattern (or changing anything else in it) fixes the problem
//
// Expected                : [ERROR] Tests run: 20, Failures: 8, Errors: 0, Skipped: 0
// Actual (without rerun)  : [ERROR] Tests run: 12, Failures: 6, Errors: 0, Skipped: 0
// Actual (with rerun)     : [ERROR] Tests run: 12, Failures: 4, Errors: 0, Skipped: 0, Flakes: 2
public class DummyTest {
  @ParameterizedTest
  @CsvSource({"yes", "no", "yes", "yes", "no"})
  public void testParameterizedWithFailures_correct_in_surefire(String param) {
    testInternal(param);
  }

  @ParameterizedTest(name = "[{index}]")
  @CsvSource({"yes", "no", "yes", "yes", "no"})
  public void testParameterizedWithFailures_mishandled_in_surefire(String param) {
    testInternal(param);
  }

  @RepeatedTest(value = 5)
  public void testRepeatedWithFailures_correct_in_surefire(RepetitionInfo repInfo) {
    switch (repInfo.getCurrentRepetition()) {
      case 2:
      case 5:
        testInternal("no");
      default:
        testInternal("yes");
    }
  }

  @RepeatedTest(value = 5, name = "[{currentRepetition}]")
  public void testRepeatedWithFailuresBroken_mishandled_in_surefire(RepetitionInfo repInfo) {
    switch (repInfo.getCurrentRepetition()) {
      case 2:
      case 5:
        testInternal("no");
      default:
        testInternal("yes");
    }
  }

  private void testInternal(String arg) {
    if (arg.equals("no")) {
      Assertions.fail("If you say 'no', it's a no");
    }
  }
}
