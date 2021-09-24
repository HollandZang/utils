package httpUtils;

import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ZnHttpUtil {
    private final ZnHttpConf znHttpConf;
    public final Sync sync;
    public final Async async;

    public ZnHttpUtil() {
        this.znHttpConf = new DefaultZnHttpConf();
        this.sync = new Sync();
        this.async = new Async();
    }

    public ZnHttpUtil(ZnHttpConf znHttpConf) {
        this.znHttpConf = znHttpConf;
        this.sync = new Sync();
        this.async = new Async();
    }

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");

    public class Sync {
        private Sync() {
        }

        public Optional<String> get(String url, Map<String, String> headers, Map<String, ?> data) {
            final HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            setEncodeQueryParam(data, urlBuilder::addEncodedQueryParameter);

            final Request request = znHttpConf.myRequest(headers)
                    .get()
                    .url(urlBuilder.build())
                    .build();

            return getOptionalString(url, data, request);
        }

        public Optional<String> postForm(String url, Map<String, String> headers, Map<String, ?> data) {
            final FormBody.Builder formBodyBuilder = new FormBody.Builder();
            setEncodeQueryParam(data, formBodyBuilder::addEncoded);

            final Request request = znHttpConf.myRequest(headers)
                    .post(formBodyBuilder.build())
                    .url(url)
                    .build();

            return getOptionalString(url, data, request);
        }

        public Optional<String> postJson(String url, Map<String, String> headers, Object data) {
            final String json = znHttpConf.toJson(data);
            final RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);

            final Request request = znHttpConf.myRequest(headers)
                    .post(requestBody)
                    .url(url)
                    .build();

            return getOptionalString(url, json, request);
        }

        private Optional<String> getOptionalString(String url, Object data, Request request) {
            try {
                final Response execute = znHttpConf.getClient().newCall(request).execute();
                final String string = execute.body().string();
                return Optional.of(string);
            } catch (Exception e) {
                znHttpConf.printError("request error: {}\nheaders: {}\ndata: {}", url, request.headers(), data, e);
                return Optional.empty();
            }
        }
    }

    public class Async {
        private Async() {
        }

        public void get(String url, Map<String, String> headers, Map<String, ?> data, Consumer<Response> onResponse) {
            final HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            setEncodeQueryParam(data, urlBuilder::addEncodedQueryParameter);

            final Request request = znHttpConf.myRequest(headers)
                    .get()
                    .url(urlBuilder.build())
                    .build();

            doCallback(url, data, request, onResponse);
        }

        public void postForm(String url, Map<String, String> headers, Map<String, ?> data, Consumer<Response> onResponse) {
            final FormBody.Builder formBodyBuilder = new FormBody.Builder();
            setEncodeQueryParam(data, formBodyBuilder::addEncoded);

            final Request request = znHttpConf.myRequest(headers)
                    .post(formBodyBuilder.build())
                    .url(url)
                    .build();

            doCallback(url, data, request, onResponse);
        }

        public void postJson(String url, Map<String, String> headers, Object data, Consumer<Response> onResponse) {
            final String json = znHttpConf.toJson(data);
            final RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);

            final Request request = znHttpConf.myRequest(headers)
                    .post(requestBody)
                    .url(url)
                    .build();

            doCallback(url, json, request, onResponse);
        }

        private void doCallback(String url, Object data, Request request, Consumer<Response> onResponse) {
            znHttpConf.getClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    znHttpConf.printError("request error: {}\nheaders: {}\ndata: {}", url, request.headers(), data, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    onResponse.accept(response);
                }
            });
        }
    }

    private void setEncodeQueryParam(Map<String, ?> data, BiConsumer<String, String> setIntoBuilder) {
        if (data != null) {
            data.forEach((k, v) -> setIntoBuilder.accept(k, znHttpConf.formatParam(v)));
        }
    }

    public static void main(String[] args) {
        final Map<String, Object> data = new HashMap<>();
        data.put("t1", LocalDateTime.now());
        data.put("t2", LocalTime.now());
        data.put("t3", LocalDate.now());

        final Optional<String> s = new ZnHttpUtil().sync.get("http://www.baidu.com", null, data);
        final String s1 = s.orElse("asd");
        System.out.println(s1);

    }
}
