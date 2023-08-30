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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class SliceToStringStrategyTests {

  @Mock private IterableToStringStrategy iterableToStringStrategy;

  @InjectMocks private SliceToStringStrategy toStringStrategy;

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
  void supports_givenSliceImpl_returnTrue() {
    assertThat(toStringStrategy.supports(new SliceImpl<TestObject>(List.of()))).isTrue();
  }

  @Test
  void supports_givenSliceableJavaProxy_returnTrue() {
    Slice<?> proxy =
        (Slice<?>)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Slice.class},
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
  void toString_givenSliceImpl_pagedAndUnsorted() {
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(
            toStringStrategy.toString(
                new SliceImpl<TestObject>(
                    List.of(new TestObject("foo")), PageRequest.of(0, 10), false)))
        .isEqualTo("[page=0,size=10,sort=UNSORTED,numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenSliceImpl_pagedAndSorted() {
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(
            toStringStrategy.toString(
                new SliceImpl<TestObject>(
                    List.of(new TestObject("foo")),
                    PageRequest.of(0, 10, Sort.by(Direction.ASC, "foo")),
                    false)))
        .isEqualTo("[page=0,size=10,sort=foo: ASC,numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenSliceImpl_unpaged() {
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(toStringStrategy.toString(new SliceImpl<TestObject>(List.of(new TestObject("foo")))))
        .isEqualTo("[numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenSliceJavaProxy_pagedAndUnsorted() {
    Slice<?> proxy =
        (Slice<?>)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Slice.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    switch (method.getName()) {
                      case "getPageable":
                        return PageRequest.of(0, 10);
                      case "getNumber":
                        return 0;
                      case "getSize":
                        return 10;
                      case "getNumberOfElements":
                        return 1;
                      case "getSort":
                        return Sort.unsorted();
                      case "getContent":
                        return List.of(new TestObject("foo"));
                      default:
                        throw new IllegalStateException();
                    }
                  }
                });
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(toStringStrategy.toString(proxy))
        .isEqualTo("[page=0,size=10,sort=UNSORTED,numberOfElements=1,content=[value=foo]]");
  }

  @Test
  void toString_givenSliceJavaProxy_unpaged() {
    Slice<?> proxy =
        (Slice<?>)
            Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class<?>[] {Slice.class},
                new InvocationHandler() {

                  @Override
                  public Object invoke(Object proxy, Method method, Object[] args)
                      throws Throwable {
                    switch (method.getName()) {
                      case "getPageable":
                        return Pageable.unpaged();
                      case "getNumberOfElements":
                        return 1;
                      case "getContent":
                        return List.of(new TestObject("foo"));
                      default:
                        throw new IllegalStateException();
                    }
                  }
                });
    when(iterableToStringStrategy.toString(any())).thenReturn("[value=foo]");

    assertThat(toStringStrategy.toString(proxy))
        .isEqualTo("[numberOfElements=1,content=[value=foo]]");
  }
}
