package im.aop.loggers.messageinterpolation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * {@link Configuration} for Spring Data Domain {@link ToStringStrategy}
 *
 * @author Andy Lian
 */
@ConditionalOnClass({Pageable.class, Slice.class, Page.class})
@Configuration
class DataDomainToStringStrategyConfiguration {

  @Bean
  public PageableToStringStrategy pageableToStringStrategy() {
    return new PageableToStringStrategy();
  }

  @Bean
  public SliceToStringStrategy sliceToStringStrategy() {
    return new SliceToStringStrategy();
  }

  @Bean
  public PageToStringStrategy pageToStringStrategy() {
    return new PageToStringStrategy();
  }
}
