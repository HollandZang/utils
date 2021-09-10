package httpUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.Map;

public interface ZnHttpConf {
    OkHttpClient getClient();

    Request.Builder myRequest(Map<String, String> headers);

    void printError(String s, Object... args);

    String formatParam(Object param);

    String toJson(Object data);
}
