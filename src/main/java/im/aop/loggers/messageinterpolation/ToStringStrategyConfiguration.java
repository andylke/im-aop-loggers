package im.aop.loggers.messageinterpolation;

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
  public ReflectionToStringStrategy reflectionToStringStrategy() {
    return new ReflectionToStringStrategy();
  }

  @Bean
  public ObjectToStringStrategy objectToStringStrategy() {
    return new ObjectToStringStrategy();
  }

  @Bean
  public OptionalToStringStrategy optionalToStringStrategy() {
    return new OptionalToStringStrategy();
  }

  @Bean
  public ArrayToStringStrategy arrayToStringStrategy() {
    return new ArrayToStringStrategy();
  }

  @Bean
  public IterableToStringStrategy iterableToStringStrategy() {
    return new IterableToStringStrategy();
  }

  @Bean
  @ConditionalOnMissingBean({ToStringStrategyFactory.class})
  public ToStringStrategyFactory toStringStrategyFactory() {
    return new DefaultToStringStrategyFactory();
  }
}
