package im.aop.loggers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for AOP Loggers.
 *
 * @author Andy Lian
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = AopLoggersProperties.PREFIX)
public class AopLoggersProperties {

  public static final String PREFIX = "im.aop.loggers";

  /** Whether to enable AOP Loggers */
  private boolean enabled = true;

  /** Log Level for entering message */
  private Level enteringLevel = Level.DEBUG;

  /** Entering message template */
  @NotBlank private String enteringMessage = "Entering [{method}] with parameters [{parameters}]";

  /** Log Level for exited normally message */
  @NotNull private Level exitedLevel = Level.DEBUG;

  /** Exited normally message template */
  @NotBlank
  private String exitedMessage = "[{method}] exited normally with return value [{return-value}]";

  /** Log Level for exited abnormally message */
  @NotNull private Level exitedAbnormallyLevel = Level.ERROR;

  /** Exited abnormally message template */
  @NotBlank
  private String exitedAbnormallyMessage =
      "[{method}] exited abnormally with exception [{exception}]";

  /** Exceptions that will be ignored by Logger */
  private Class<? extends Throwable>[] ignoreExceptions;

  /** Log Level for elapsed message */
  @NotNull private Level elapsedLevel = Level.DEBUG;

  /** Elapsed message template */
  @NotBlank private String elapsedMessage = "[{method}] elapsed [{elapsed}]";

  /** Log Level for elapsed warning message */
  @NotNull private Level elapsedWarningLevel = Level.WARN;

  /** Elapsed warning message template */
  @NotBlank
  private String elapsedWarningMessage =
      "[{method}] reached elapsed time limit [{elapsed-time-limit}]";

  /** Log Level for committing transaction message */
  @NotNull private Level committingTransactionLevel = Level.DEBUG;

  /** Committing transaction message template */
  @NotBlank
  private String committingTransactionMessage =
      "Committing transaction for [{method}] with parameters [{parameters}]";

  /** Log Level for committed transaction message */
  @NotNull private Level transactionCommittedLevel = Level.DEBUG;

  /** Transaction committed message template */
  @NotBlank
  private String transactionCommittedMessage =
      "Transaction committed successfully for [{method}] with parameters [{parameters}]";

  /** Log Level for rollbacked transaction message */
  @NotNull private Level transactionRollbackedLevel = Level.DEBUG;

  /** Transaction rollbacked message template */
  @NotBlank
  private String transactionRollbackedMessage =
      "Transaction rollbacked for [{method}] with parameters [{parameters}]";
}
