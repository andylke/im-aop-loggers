package im.aop.loggers.messageinterpolation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
    prefix = "im.aop.loggers",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
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
