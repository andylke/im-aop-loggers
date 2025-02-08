package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * Tests for {@link PageableToStringStrategy}.
 *
 * @author Andy Lian
 */
@ExtendWith(MockitoExtension.class)
class PageableToStringStrategyTests {

  @InjectMocks private PageableToStringStrategy toStringStrategy;

  @Test
  void supports_givenNull_returnFalse() {
    assertThat(toStringStrategy.supports(null)).isFalse();
  }

  @Test
  void supports_givenObject_returnFalse() {
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  @Test
  void supports_givenPageRequest_returnTrue() {
    assertThat(toStringStrategy.supports(PageRequest.ofSize(10))).isTrue();
  }

  @Test
  void supports_givenPageableJavaProxy_returnTrue() {
    Pageable proxy =
        (Pageable)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Pageable.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return "foo";
                  }
                });

    assertThat(toStringStrategy.supports(proxy)).isTrue();
  }

  public interface TestInterface {

    String getValue();
  }

  @Test
  void supports_givenTestInterfaceJavaProxy_returnFalse() {
    TestInterface proxy =
        (TestInterface)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {TestInterface.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return "foo";
                  }
                });

    assertThat(toStringStrategy.supports(proxy)).isFalse();
  }

  @Test
  void toString_givenNull_throwException() {
    assertThrows(NullPointerException.class, () -> toStringStrategy.toString(null));
  }

  @Test
  void toString_givenObject_throwException() {
    assertThrows(ClassCastException.class, () -> toStringStrategy.toString(new Object()));
  }

  @Test
  void toString_givenPageRequest_pagedAndUnsorted() {
    assertThat(toStringStrategy.toString(PageRequest.of(0, 10)))
        .isEqualTo("[page=0,size=10,sort=UNSORTED]");
  }

  @Test
  void toString_givenPageRequest_pagedAndSorted() {
    assertThat(toStringStrategy.toString(PageRequest.of(0, 10, Sort.by(Direction.ASC, "foo"))))
        .isEqualTo("[page=0,size=10,sort=foo: ASC]");
  }

  @Test
  void toString_givenPageRequest_unpaged() {
    assertThat(toStringStrategy.toString(Pageable.unpaged())).isEqualTo("[UNPAGED]");
  }

  @Test
  void toString_givenPageableJavaProxy_pagedAndUnsorted() {
    Pageable proxy =
        (Pageable)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Pageable.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return switch (method.getName()) {
                      case "isPaged" -> true;
                      case "getPageNumber" -> 0;
                      case "getPageSize" -> 10;
                      case "getSort" -> Sort.unsorted();
                      case "hashCode" -> -1;
                      default -> throw new IllegalStateException();
                    };
                  }
                });

    assertThat(toStringStrategy.toString(proxy)).isEqualTo("[page=0,size=10,sort=UNSORTED]");
  }

  @Test
  void toString_givenPageableJavaProxy_pagedAndSorted() {
    assertThat(toStringStrategy.toString(PageRequest.of(0, 10, Sort.by(Direction.ASC, "foo"))))
        .isEqualTo("[page=0,size=10,sort=foo: ASC]");
  }
}
