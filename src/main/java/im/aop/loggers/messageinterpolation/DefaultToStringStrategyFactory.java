package im.aop.loggers.messageinterpolation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;

/**
 * Default {@link ToStringStrategyFactory} implementation.
 *
 * @author Andy Lian
 */
public class DefaultToStringStrategyFactory implements ToStringStrategyFactory {

  @Autowired private ObjectToStringStrategy objectToStringStrategy;

  @Autowired private ObjectProvider<ToStringStrategy> toStringStrategiesProvider;

  private List<ToStringStrategy> toStringStrategies = new ArrayList<>();

  @PostConstruct
  void postConstruct() {
    toStringStrategies =
        toStringStrategiesProvider.stream()
            .filter(toStringStrategy -> toStringStrategy instanceof ObjectToStringStrategy == false)
            .collect(Collectors.toList());
  }

  @Override
  public ToStringStrategy findOrDefault(Object object) {
    if (object == null || toStringStrategies == null || toStringStrategies.isEmpty()) {
      return objectToStringStrategy;
    }

    return toStringStrategies.parallelStream()
        .filter(toStringStrategy -> toStringStrategy.supports(object))
        .findFirst()
        .orElse(objectToStringStrategy);
  }
}
