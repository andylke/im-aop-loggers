package im.aop.loggers.logging.message;

/**
 * Substitute variables within a template String.
 *
 * @author Andy Lian
 */
public class StringSubstitutor {

  private static final char VARIABLE_START_CHAR = '{';

  private static final char VARIABLE_END_CHAR = '}';

  private static final String EMPTY_STRING = "";

  public String substitute(final String template, final StringLookup stringLookup) {
    if (template == null || template.length() == 0) {
      return template;
    }

    final StringBuilder resultBuilder = new StringBuilder();

    int templateCursor = 0;
    do {
      final int variableEndCursor = nextEndTokenCursor(template, templateCursor);
      if (variableEndCursor < 0) {
        break;
      }
      final int variableStartCursor =
          nextStartVariableCursor(template, templateCursor, variableEndCursor);
      if (variableStartCursor < 0) {
        break;
      }

      templateCursor =
          appendToBuilder(resultBuilder, template, templateCursor, variableStartCursor);
      String tokenValue =
          lookupVariableOrEmpty(template, variableStartCursor, variableEndCursor, stringLookup);
      appendToBuilder(resultBuilder, tokenValue);

      templateCursor = variableEndCursor + 1;
    } while (templateCursor < template.length());

    appendToBuilder(resultBuilder, template, templateCursor, template.length());
    return resultBuilder.toString();
  }

  private int nextEndTokenCursor(final String template, final int variableStartCursor) {
    return template.indexOf(VARIABLE_END_CHAR, variableStartCursor + 1);
  }

  private int nextStartVariableCursor(
      final String template, final int templateCursor, final int variableEndCursor) {
    for (int cursor = variableEndCursor - 1; cursor >= templateCursor; cursor--) {
      if (template.charAt(cursor) == VARIABLE_START_CHAR) {
        return cursor;
      }
    }

    return -1;
  }

  private int appendToBuilder(
      final StringBuilder builder,
      final String template,
      final int startCursor,
      final int endCursor) {
    builder.append(template, startCursor, endCursor);
    return endCursor + 1;
  }

  private void appendToBuilder(final StringBuilder builder, final String value) {
    builder.append(value);
  }

  private String lookupVariableOrEmpty(
      final String template,
      final int variableStartCursor,
      final int variableEndCursor,
      final StringLookup stringLookup) {
    final String key = template.substring(variableStartCursor + 1, variableEndCursor);
    final String value = stringLookup.lookup(key);
    return value != null ? value : EMPTY_STRING;
  }
}
