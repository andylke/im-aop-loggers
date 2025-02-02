package im.aop.loggers.messageinterpolation;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

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
            .filter(toStringStrategy -> !(toStringStrategy instanceof ObjectToStringStrategy))
            .toList();
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
