package im.aop.loggers;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import im.aop.loggers.advice.afterreturning.LogAfterReturningConfiguration;
import im.aop.loggers.advice.afterthrowing.LogAfterThrowingConfiguration;
import im.aop.loggers.advice.around.LogAroundConfiguration;
import im.aop.loggers.advice.before.LogBeforeConfiguration;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;

@EnableConfigurationProperties({AopLoggersProperties.class})
@Import({
  StringSubstitutorConfiguration.class,
  LogAfterReturningConfiguration.class,
  LogAfterThrowingConfiguration.class,
  LogAroundConfiguration.class,
  LogBeforeConfiguration.class
})
public class AopLoggersConfiguration {}
