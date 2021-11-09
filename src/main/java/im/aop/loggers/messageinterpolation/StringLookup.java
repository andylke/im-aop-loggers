package im.aop.loggers.messageinterpolation;

/**
 * Used by {@link StringSubstitutor} to lookup for String value by key.
 *
 * @author Andy Lian
 */
public interface StringLookup {

  String lookup(String key);
}
