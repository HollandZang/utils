//package httpUtils;
//
//import com.alibaba.fastjson.JSON;
//import okhttp3.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.Temporal;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//import java.util.function.BiConsumer;
//import java.util.function.Consumer;
//
//public class HttpUtil {
//    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
//
//    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=UTF-8");
//
//    protected static final OkHttpClient CLIENT = new OkHttpClient.Builder()
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .build();
//
//    protected static Request.Builder myRequest(Map<String, String> headers) {
//        Request.Builder builder = new Request.Builder()
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Accept", "*/*");
//        if (headers != null) {
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                builder.addHeader(entry.getKey(), entry.getValue());
//            }
//        }
//        return builder;
//    }
//
//    public static class Sync {
//        public static Optional<String> get(String url, Map<String, String> headers, Map<String, ?> data) {
//            final HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
//            setEncodeQueryParam(data, urlBuilder::addEncodedQueryParameter);
//
//            final Request request = myRequest(headers)
//                    .get()
//                    .url(urlBuilder.build())
//                    .build();
//
//            return getOptionalString(url, data, request);
//        }
//
//        public static Optional<String> postForm(String url, Map<String, String> headers, Map<String, ?> data) {
//            final FormBody.Builder formBodyBuilder = new FormBody.Builder();
//            setEncodeQueryParam(data, formBodyBuilder::addEncoded);
//
//            final Request request = myRequest(headers)
//                    .post(formBodyBuilder.build())
//                    .url(url)
//                    .build();
//
//            return getOptionalString(url, data, request);
//        }
//
//        public static Optional<String> postJson(String url, Map<String, String> headers, Object data) {
//            final String json = data != null ? JSON.toJSONString(data) : "";
//            final RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
//
//            final Request request = myRequest(headers)
//                    .post(requestBody)
//                    .url(url)
//                    .build();
//
//            return getOptionalString(url, json, request);
//        }
//
//        private static Optional<String> getOptionalString(String url, Object data, Request request) {
//            try {
//                final Response execute = CLIENT.newCall(request).execute();
//                final String string = execute.body().string();
//                return Optional.of(string);
//            } catch (Exception e) {
//                logger.error("request error: {}\nheaders: {}\ndata: {}", url, request.headers(), data, e);
//                return Optional.empty();
//            }
//        }
//    }
//
//    public static class Async {
//        public static void get(String url, Map<String, String> headers, Map<String, ?> data, Consumer<Response> onResponse) {
//            final HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
//            setEncodeQueryParam(data, urlBuilder::addEncodedQueryParameter);
//
//            final Request request = myRequest(headers)
//                    .get()
//                    .url(urlBuilder.build())
//                    .build();
//
//            doCallback(url, data, request, onResponse);
//        }
//
//        public static void postForm(String url, Map<String, String> headers, Map<String, ?> data, Consumer<Response> onResponse) {
//            final FormBody.Builder formBodyBuilder = new FormBody.Builder();
//            setEncodeQueryParam(data, formBodyBuilder::addEncoded);
//
//            final Request request = myRequest(headers)
//                    .post(formBodyBuilder.build())
//                    .url(url)
//                    .build();
//
//            doCallback(url, data, request, onResponse);
//        }
//
//        public static void postJson(String url, Map<String, String> headers, Object data, Consumer<Response> onResponse) {
//            final String json = data != null ? JSON.toJSONString(data) : "";
//            final RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
//
//            final Request request = myRequest(headers)
//                    .post(requestBody)
//                    .url(url)
//                    .build();
//
//            doCallback(url, json, request, onResponse);
//        }
//
//        private static void doCallback(String url, Object data, Request request, Consumer<Response> onResponse) {
//            CLIENT.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    logger.error("request error: {}\nheaders: {}\ndata: {}", url, request.headers(), data, e);
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    onResponse.accept(response);
//                }
//            });
//        }
//    }
//
//    private static void setEncodeQueryParam(Map<String, ?> data, BiConsumer<String, String> setIntoBuilder) {
//        if (data != null) {
//            data.forEach((k, v) -> {
//                if (v instanceof Date) {
//                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    final String format = formatter.format((Date) v);
//                    setIntoBuilder.accept(k, format);
//                } else if (v instanceof LocalDateTime) {
//                    final String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((Temporal) v);
//                    setIntoBuilder.accept(k, format);
//                } else if (v instanceof LocalDate) {
//                    final String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format((Temporal) v);
//                    setIntoBuilder.accept(k, format);
//                } else if (v instanceof LocalTime) {
//                    final String format = DateTimeFormatter.ofPattern("HH:mm:ss").format((Temporal) v);
//                    setIntoBuilder.accept(k, format);
//                } else {
//                    setIntoBuilder.accept(k, v.toString());
//                }
//            });
//        }
//    }
//
//    public static void main(String[] args) {
//        final Map<String, Object> data = new HashMap<>();
//        data.put("t1", LocalDateTime.now());
//        data.put("t2", LocalTime.now());
//        data.put("t3", LocalDate.now());
//        Async.get("http://www.baidu.com", null, data, response -> {
//            try {
//                System.out.println(response.body().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//}
