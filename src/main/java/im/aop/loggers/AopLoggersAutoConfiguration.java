package im.aop.loggers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for AOP
 * Loggers.
 *
 * @author Andy Lian
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
    prefix = AopLoggersProperties.PREFIX,
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Import(AopLoggersConfiguration.class)
public class AopLoggersAutoConfiguration {}
