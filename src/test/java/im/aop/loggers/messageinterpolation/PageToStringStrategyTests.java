package im.aop.loggers.messageinterpolation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class PageToStringStrategyTests {

  @Mock private IterableToStringStrategy iterableToStringStrategy;

  @InjectMocks private PageToStringStrategy toStringStrategy;

  @Test
  void supports_givenNull_returnFalse() {
    assertThat(toStringStrategy.supports(null)).isFalse();
  }

  @Test
  void supports_givenObject_returnFalse() {
    assertThat(toStringStrategy.supports(new Object())).isFalse();
  }

  static class TestObject {

    private String value;

    public TestObject(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  @Test
  void supports_givenPageImpl_returnTrue() {
    assertThat(toStringStrategy.supports(new PageImpl<TestObject>(List.of()))).isTrue();
  }

  @Test
  void supports_givenPageableJavaProxy_returnTrue() {
    Page<?> proxy =
        (Page<?>)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Page.class},
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
    var object = new Object();
    assertThrows(ClassCastException.class, () -> toStringStrategy.toString(object));
  }

  @Test
  void toString_givenPageImpl_pagedAndUnsorted() {
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(
            toStringStrategy.toString(
                new PageImpl<TestObject>(List.of(new TestObject("foo")), PageRequest.of(0, 10), 1)))
        .isEqualTo(
            "[page=0,size=10,sort=UNSORTED,totalElements=1,totalPages=1,numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenPageImpl_pagedAndSorted() {
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(
            toStringStrategy.toString(
                new PageImpl<TestObject>(
                    List.of(new TestObject("foo")),
                    PageRequest.of(0, 10, Sort.by(Direction.ASC, "foo")),
                    1)))
        .isEqualTo(
            "[page=0,size=10,sort=foo: ASC,totalElements=1,totalPages=1,numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenPageImpl_unpaged() {
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(toStringStrategy.toString(new PageImpl<TestObject>(List.of(new TestObject("foo")))))
        .isEqualTo("[numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenPageJavaProxy_pagedAndUnsorted() {
    Page<?> proxy =
        (Page<?>)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Page.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return switch (method.getName()) {
                      case "getPageable" -> PageRequest.of(0, 10);
                      case "getNumber" -> 0;
                      case "getSize" -> 10;
                      case "getTotalPages" -> 1;
                      case "getNumberOfElements" -> 1;
                      case "getTotalElements" -> 1L;
                      case "getSort" -> Sort.unsorted();
                      case "getContent" -> List.of(new TestObject("foo"));
                      case "hashCode" -> -1;
                      default -> throw new IllegalStateException();
                    };
                  }
                });
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(toStringStrategy.toString(proxy))
        .isEqualTo(
            "[page=0,size=10,sort=UNSORTED,totalElements=1,totalPages=1,numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenPageJavaProxy_unpaged() {
    Page<?> proxy =
        (Page<?>)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Page.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    return switch (method.getName()) {
                      case "getPageable" -> Pageable.unpaged();
                      case "getNumberOfElements" -> 1;
                      case "getContent" -> List.of(new TestObject("foo"));
                      case "hashCode" -> -1;
                      default -> throw new IllegalStateException();
                    };
                  }
                });
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(toStringStrategy.toString(proxy))
        .isEqualTo("[numberOfElements=1,content=[value=foo]]");
  }
}
