package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalIPUtil {

    /**
     * IP 地址校验的正则表达式
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    /**
     * 获取 IP 地址的服务列表
     */
    private static final String[] IPV4_SERVICES = {
            "http://checkip.amazonaws.com/",
            "https://ipv4.icanhazip.com/",
            "http://bot.whatismyipaddress.com/"
            // and so on ...
    };

    public static String get() throws ExecutionException, InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();
        for (String ipService : IPV4_SERVICES) {
            callables.add(() -> get(ipService));
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            // 返回第一个成功获取的 IP
            return executorService.invokeAny(callables);
        } finally {
            executorService.shutdown();
        }
    }

    private static String get(String url) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String ip = in.readLine();
            if (IPV4_PATTERN.matcher(ip).matches()) {
                return ip;
            } else {
                throw new IOException("invalid IPv4 address: " + ip);
            }
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        System.out.println(get());
        System.out.println(getNowIP1());
    }

    private static String getNowIP1() throws IOException {
        String ip = null;
        String chinaz = "https://ip.chinaz.com/";
        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
            Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
            Matcher m = p.matcher(inputLine.toString());
            if (m.find()) {
                ip = m.group(1);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        if (null == ip || ip.length() == 0) {
            throw new RuntimeException();
        }
        return ip;
    }
}