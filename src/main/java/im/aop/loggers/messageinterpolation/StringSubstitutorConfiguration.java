package im.aop.loggers.messageinterpolation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link Configuration} for {@link StringSubstitutor} and {@link StringSupplierRegistrar}.
 *
 * @author Andy Lian
 */
@Configuration(proxyBeanMethods = false)
@Import({DataDomainToStringStrategyConfiguration.class, ToStringStrategyConfiguration.class})
public class StringSubstitutorConfiguration {

  @Bean
  public StringSubstitutor stringSubstitutor() {
    return new StringSubstitutor();
  }

  @Bean
  public ElapsedStringSupplierRegistrar elapsedStringSupplierRegistrar() {
    return new ElapsedStringSupplierRegistrar();
  }

  @Bean
  public ElapsedTimeLimitStringSupplierRegistrar elapsedTimeLimitStringSupplierRegistrar() {
    return new ElapsedTimeLimitStringSupplierRegistrar();
  }

  @Bean
  public ExceptionStringSupplierRegistrar exceptionStringSupplierRegistrar() {
    return new ExceptionStringSupplierRegistrar();
  }

  @Bean
  public JoinPointStringSupplierRegistrar joinPointStringSupplierRegistrar() {
    return new JoinPointStringSupplierRegistrar();
  }

  @Bean
  public ReturnValueStringSupplierRegistrar returnValueStringSupplierRegistrar() {
    return new ReturnValueStringSupplierRegistrar();
  }
}
