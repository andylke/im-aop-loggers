package im.aop.loggers.logging.message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Default {@link StringLookup} used by {@link StringSubstitutor} to lookup for String value by key.
 *
 * @author Andy Lian
 */
public class StringSupplierLookup implements StringLookup {

  private final Map<String, Supplier<String>> map = new HashMap<String, Supplier<String>>();

  @Override
  public String lookup(final String key) {
    return map.containsKey(key) ? map.get(key).get() : null;
  }

  public void addStringSupplier(final String key, final Supplier<String> stringSupplier) {
    map.put(key, stringSupplier);
  }
}
