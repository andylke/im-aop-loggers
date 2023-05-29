package im.aop.loggers.messageinterpolation;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link Configuration} for {@link ToStringStrategy}.
 *
 * @author Andy Lian
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ReflectionToStringProperties.class})
class ToStringStrategyConfiguration {

  @Bean
  public ReflectionToStringStrategy reflectionToStringStrategy(
      final ReflectionToStringProperties reflectionToStringProperties) {
    return new ReflectionToStringStrategy(reflectionToStringProperties);
  }

  @Bean
  public ObjectToStringStrategy objectToStringStrategy() {
    return new ObjectToStringStrategy();
  }

  @Bean
  public OptionalToStringStrategy optionalToStringStrategy(
      final ObjectToStringStrategy objectToStringStrategy) {
    return new OptionalToStringStrategy(objectToStringStrategy);
  }

  @Bean
  public ArrayToStringStrategy arrayToStringStrategy(
      final ObjectToStringStrategy objectToStringStrategy) {
    return new ArrayToStringStrategy(objectToStringStrategy);
  }

  @Bean
  public IterableToStringStrategy iterableToStringStrategy(
      final ObjectToStringStrategy objectToStringStrategy) {
    return new IterableToStringStrategy(objectToStringStrategy);
  }

  @Bean
  @ConditionalOnMissingBean({ToStringStrategyFactory.class})
  public ToStringStrategyFactory toStringStrategyFactory(
      final ObjectToStringStrategy objectToStringStrategy,
      final List<ToStringStrategy> toStringStrategies) {
    return new DefaultToStringStrategyFactory(objectToStringStrategy, toStringStrategies);
  }
}
