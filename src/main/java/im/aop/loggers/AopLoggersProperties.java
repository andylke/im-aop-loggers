package im.aop.loggers;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import im.aop.loggers.logging.Level;

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

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Level getEnteringLevel() {
    return enteringLevel;
  }

  public void setEnteringLevel(Level enteringLevel) {
    this.enteringLevel = enteringLevel;
  }

  public String getEnteringMessage() {
    return enteringMessage;
  }

  public void setEnteringMessage(String enteringMessage) {
    this.enteringMessage = enteringMessage;
  }

  public Level getExitedLevel() {
    return exitedLevel;
  }

  public void setExitedLevel(Level exitedLevel) {
    this.exitedLevel = exitedLevel;
  }

  public String getExitedMessage() {
    return exitedMessage;
  }

  public void setExitedMessage(String exitedMessage) {
    this.exitedMessage = exitedMessage;
  }

  public Level getExitedAbnormallyLevel() {
    return exitedAbnormallyLevel;
  }

  public void setExitedAbnormallyLevel(Level exitedAbnormallyLevel) {
    this.exitedAbnormallyLevel = exitedAbnormallyLevel;
  }

  public String getExitedAbnormallyMessage() {
    return exitedAbnormallyMessage;
  }

  public void setExitedAbnormallyMessage(String exitedAbnormallyMessage) {
    this.exitedAbnormallyMessage = exitedAbnormallyMessage;
  }

  public Class<? extends Throwable>[] getIgnoreExceptions() {
    return ignoreExceptions;
  }

  public void setIgnoreExceptions(Class<? extends Throwable>[] ignoreExceptions) {
    this.ignoreExceptions = ignoreExceptions;
  }

  public Level getElapsedLevel() {
    return elapsedLevel;
  }

  public void setElapsedLevel(Level elapsedLevel) {
    this.elapsedLevel = elapsedLevel;
  }

  public String getElapsedMessage() {
    return elapsedMessage;
  }

  public void setElapsedMessage(String elapsedMessage) {
    this.elapsedMessage = elapsedMessage;
  }

  public Level getElapsedWarningLevel() {
    return elapsedWarningLevel;
  }

  public void setElapsedWarningLevel(Level elapsedWarningLevel) {
    this.elapsedWarningLevel = elapsedWarningLevel;
  }

  public String getElapsedWarningMessage() {
    return elapsedWarningMessage;
  }

  public void setElapsedWarningMessage(String elapsedWarningMessage) {
    this.elapsedWarningMessage = elapsedWarningMessage;
  }
}
