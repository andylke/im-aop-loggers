package im.aop.loggers.logging.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StringSubstitutor}.
 *
 * @author Andy Lian
 */
class StringSubstitutorTests {

  private static final StringSupplierLookup STRING_LOOKUP = new StringSupplierLookup();

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  @BeforeAll
  static void configure() {
    STRING_LOOKUP.addStringSupplier("foo", () -> "foo");
    STRING_LOOKUP.addStringSupplier("bar", () -> "bar");
  }

  @Test
  void substitute_textStartsWithToken() {
    final String result = STRING_SUBSTITUTOR.substitute("{foo} bar", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo bar");
  }

  @Test
  void substitute_textEndsWithToken() {
    final String result = STRING_SUBSTITUTOR.substitute("foo {bar}", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo bar");
  }

  @Test
  void substitute_tokenInBetweenText() {
    final String result = STRING_SUBSTITUTOR.substitute("foo {bar} baz", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo bar baz");
  }

  @Test
  void substitute_missingStartTokenChar() {
    final String result = STRING_SUBSTITUTOR.substitute("foo bar} baz", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo bar} baz");
  }

  @Test
  void substitute_missingEndTokenChar() {
    final String result = STRING_SUBSTITUTOR.substitute("foo {bar baz", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo {bar baz");
  }

  @Test
  void substitute_twoStartTokenChar() {
    final String result = STRING_SUBSTITUTOR.substitute("foo {{bar} baz", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo {bar baz");
  }

  @Test
  void substitute_twoEndTokenChar() {
    final String result = STRING_SUBSTITUTOR.substitute("foo {bar}} baz", STRING_LOOKUP);
    assertThat(result).isEqualTo("foo bar} baz");
  }

  @Test
  void substitute_nullTemplate() {
    final String result = STRING_SUBSTITUTOR.substitute(null, STRING_LOOKUP);
    assertThat(result).isNull();
  }

  @Test
  void substitute_emptyTemplate() {
    final String result = STRING_SUBSTITUTOR.substitute("", STRING_LOOKUP);
    assertThat(result).isEmpty();
  }

  @Test
  void substitute_tokenNotFound() {
    final String result = STRING_SUBSTITUTOR.substitute("{qux}", STRING_LOOKUP);
    assertThat(result).isEmpty();
  }
}
