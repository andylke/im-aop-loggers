package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringSubstitutorPerformanceTests {

  private StringSubstitutor stringSubstitutor;

  private StringSupplierLookup stringLookup;

  private String stringTemplate;

  @BeforeEach
  void beforeEach() {
    stringSubstitutor = new StringSubstitutor();
    stringLookup = new StringSupplierLookup();
    stringLookup.addStringSupplier("foo", () -> "foo");
    stringLookup.addStringSupplier("bar", () -> "bar");
    stringTemplate = "stringSubstitutor performance testing with [{foo}] and [{bar}]";
  }

  @Test
  void substitute10000() {
    final String result = stringSubstitutor.substitute(stringTemplate, stringLookup);
    assertThat(result).isEqualTo("stringSubstitutor performance testing with [foo] and [bar]");

    for (int index = 0; index < 10000; index++) {
      stringSubstitutor.substitute(stringTemplate, stringLookup);
    }
  }
}
