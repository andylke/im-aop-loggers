package im.aop.loggers.messageinterpolation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default {@link ToStringStrategyFactory} implementation.
 *
 * @author Andy Lian
 */
public class DefaultToStringStrategyFactory implements ToStringStrategyFactory {

  private final ObjectToStringStrategy objectToStringStrategy;

  private final List<ToStringStrategy> toStringStrategies;

  public DefaultToStringStrategyFactory(
      final ObjectToStringStrategy objectToStringStrategy,
      final List<ToStringStrategy> toStringStrategies) {
    this.objectToStringStrategy = Objects.requireNonNull(objectToStringStrategy);
    this.toStringStrategies =
        toStringStrategies == null
            ? null
            : toStringStrategies
                .stream()
                .filter(
                    toStringStrategy -> toStringStrategy instanceof ObjectToStringStrategy == false)
                .collect(Collectors.toList());
  }

  @Override
  public ToStringStrategy findOrDefault(Object object) {
    if (object == null || toStringStrategies == null || toStringStrategies.isEmpty()) {
      return objectToStringStrategy;
    }

    return toStringStrategies
        .parallelStream()
        .filter(toStringStrategy -> toStringStrategy.supports(object.getClass()))
        .findFirst()
        .orElse(objectToStringStrategy);
  }
}
