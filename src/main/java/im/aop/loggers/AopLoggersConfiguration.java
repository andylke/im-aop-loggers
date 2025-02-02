package im.aop.loggers;

import im.aop.loggers.advice.aftercommit.LogAfterCommitConfiguration;
import im.aop.loggers.advice.afterreturning.LogAfterReturningConfiguration;
import im.aop.loggers.advice.afterrollback.LogAfterRollbackConfiguration;
import im.aop.loggers.advice.afterthrowing.LogAfterThrowingConfiguration;
import im.aop.loggers.advice.around.LogAroundConfiguration;
import im.aop.loggers.advice.before.LogBeforeConfiguration;
import im.aop.loggers.advice.beforecommit.LogBeforeCommitConfiguration;
import im.aop.loggers.messageinterpolation.StringSubstitutorConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties({AopLoggersProperties.class})
@Import({
  StringSubstitutorConfiguration.class,
  LogAfterReturningConfiguration.class,
  LogAfterThrowingConfiguration.class,
  LogAroundConfiguration.class,
  LogBeforeConfiguration.class,
  LogBeforeCommitConfiguration.class,
  LogAfterCommitConfiguration.class,
  LogAfterRollbackConfiguration.class
})
public class AopLoggersConfiguration {}
