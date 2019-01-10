package okhttp3;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * Not 100% functional yet
 * TODO: extend OkHttp implementations
 */
public class CallBuilder {

  private FormBody.Builder formBuilder = null;
  private Request.Builder requestBuilder;
  private StringBuilder query;
  private String url;
  private Method method;

  public CallBuilder() {
    requestBuilder = new Request.Builder();
    query = new StringBuilder();
  }

  public CallBuilder header(String name, @Nullable String value) {
    if (value != null)
      requestBuilder.header(name, value);
    return this;
  }

  public CallBuilder url(String url) throws IOException {
    this.url = url;
    return this;
  }

  public CallBuilder method(Method method) {
    this.method = method;
    return this;
  }

  public CallBuilder field(String name, @Nullable String value) {
    createFormBuilder();
    if (value != null)
      formBuilder.add(name, value);
    return this;
  }

  public CallBuilder field(String name, @Nullable Long value) {
    if (value != null)
      field(name, value.toString());
    return this;
  }

  public CallBuilder field(String name, @Nullable Object value) {
    if (value != null)
      field(name, value.toString());
    return this;
  }

  // TODO upload any file
  public CallBuilder field(String name, @Nullable File value) {
    if (formBuilder == null)
      createFormBuilder();
    requestBuilder.post(new MultipartBody.Builder()
                            //                .addFormDataPart()
                            .build()
    );
    return this;
  }

  public CallBuilder query(String name, @Nullable String value) {
    if (value != null)
      if (!value.equalsIgnoreCase("null"))
        query.append(query.toString().isEmpty() ? "?" : "&")
            .append(name)
            .append("=")
            .append(value);
    return this;
  }

  /**
   * TODO add function accepting map as parameter
   */

  public CallBuilder addEncoded(String name, @Nullable String value) {
    createFormBuilder();
    if (value != null)
      formBuilder.add(name, value);
    formBuilder.addEncoded(name, value);
    return this;
  }

  // TODO reassess
  public Call build() {
    url += query.toString();
    System.out.println(url);
    requestBuilder.url(url);
    FormBody formBody = null;
    if (formBuilder != null)
      formBody = formBuilder.build();
    requestBuilder.method(method.getMethod(), formBody);
    Request request = requestBuilder.build();
    return new OkHttpClient().newCall(request);
  }

  private void createFormBuilder() {
    if (formBuilder == null) {
      formBuilder = new FormBody.Builder();
    }
  }

  public enum Method {
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),
    PUT("PUT");

    private String method;

    Method(String s) {
      method = s;
    }

    private String getMethod() {
      return method;
    }
  }
}
