package com.xuehai.test.http;

import com.xuehai.test.base.Configuration;
import com.xuehai.test.base.Log;
import com.xuehai.test.utils.CommonUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName HttpClientDispatch
 * @Description:    HttpClient客户端
 * @Author Sniper
 * @Date 2019/3/14 15:01
 */
public class HttpClientDispatch {
    private static final String CLASS_NAME = HttpClientDispatch.class.getName();
    private static RequestConfig requestConfig;
    private static HttpClientDispatch instance;

    public static HttpClientDispatch getInstance() {
        if (instance == null) {
            instance = new HttpClientDispatch();
            initRequestConfig();
        }
        return instance;
    }

    public static void initRequestConfig() {
        Log.info(CLASS_NAME, "初始化HttpClient配置");
        Map httpConfig = (Map) Configuration.getConfig().get("http-client");
        RequestConfig.Builder builder = RequestConfig.custom();
        httpConfig.forEach((key, value) -> {
            switch(key.toString()) {
                case "proxy" :
                    Map httpProxy = (Map) value;
                    Object host = httpProxy.get("host");
                    Object port = httpProxy.get("port");
                    if (host != null && port != null) {
                        HttpHost httpHost = new HttpHost((String) httpProxy.get("host"), (Integer) httpProxy.get("port"));
                        builder.setProxy(httpHost);
                    }
                    break;
                case "socket-timeout" :
                    if (value != null) {
                        builder.setSocketTimeout((Integer) value);
                    }
                    break;
                case "connect-timeout" :
                    if (value != null) {
                        builder.setConnectTimeout((Integer) value);
                    }
                    break;
                case "connection-request-timeout" :
                    if (value != null) {
                        builder.setConnectionRequestTimeout((Integer) value);
                    }
                    break;
                default : break;
            }
            requestConfig = builder.build();
        });
        Log.info(CLASS_NAME, "HttpClient配置初始化成功");
    }

    public String sendHttpGet(String httpUrl)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpGet(httpUrl);
        return sendHttpRequest(httpRequest, new HashMap<>());
    }

    public String sendHttpGet(String httpUrl, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpGet(httpUrl);
        return sendHttpRequest(httpRequest, header);
    }

    public String sendHttpPost(String httpUrl)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPost(httpUrl);
        return sendHttpRequest(httpRequest, new HashMap<>());
    }

    public String sendHttpPost(String httpUrl, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPost(httpUrl);
        return sendHttpRequest(httpRequest, header);
    }

    public String sendHttpPost(String httpUrl, String body, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPost(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        ((HttpPost) httpRequest).setEntity(stringEntity);
        return sendHttpRequest(httpRequest, header);
    }

    public String sendHttpPost(String httpUrl, Map<String, File> map, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPost(httpUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for(Map.Entry<String, File> entry : map.entrySet()){
            String key = entry.getKey();
            File file = entry.getValue();
            FileBody fileBody = new FileBody(file);
            builder.addPart(key, fileBody);
        }
        HttpEntity reqEntity = builder.build();
        ((HttpPost) httpRequest).setEntity(reqEntity);
        return sendHttpRequest(httpRequest, header);
    }

    public String sendHttpDelete(String httpUrl)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpDelete(httpUrl);
        return sendHttpRequest(httpRequest, new HashMap<>());
    }

    public String sendHttpDelete(String httpUrl, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpDelete(httpUrl);
        return sendHttpRequest(httpRequest, header);
    }

    public String sendHttpDelete(String httpUrl, String body, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpDelete(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        ((HttpDelete) httpRequest).setEntity(stringEntity);
        return sendHttpRequest(httpRequest, header);
    }



    public String sendHttpPut(String httpUrl)
            throws ParseException, IOException {
        HttpRequestBase httpPut = new HttpPut(httpUrl);
        return sendHttpRequest(httpPut, new HashMap<>());
    }

    public String sendHttpPut(String httpUrl, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpPut = new HttpPut(httpUrl);
        return sendHttpRequest(httpPut, header);
    }

    public String sendHttpPut(String httpUrl, String body, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpPut = new HttpPut(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        ((HttpPut) httpPut).setEntity(stringEntity);
        return sendHttpRequest(httpPut, header);
    }

    public String sendHttpPatch(String httpUrl)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPatch(httpUrl);
        return sendHttpRequest(httpRequest, new HashMap<>());
    }

    public String sendHttpPatch(String httpUrl, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPatch(httpUrl);
        return sendHttpRequest(httpRequest, header);
    }

    public String sendHttpPatch(String httpUrl, String body, Map<String, String> header)
            throws ParseException, IOException {
        HttpRequestBase httpRequest = new HttpPatch(httpUrl);
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        ((HttpPatch) httpRequest).setEntity(stringEntity);
        return sendHttpRequest(httpRequest, header);
    }

    private String sendHttpRequest(HttpRequestBase httpRequest, Map<String, String> header)
            throws ParseException, IOException {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseInfo = null;
        try {
            httpClient = HttpClients.createDefault();
            httpRequest.setConfig(requestConfig);
            for(Map.Entry<String, String> entry: header.entrySet()){
                String key=entry.getKey();
                String value=entry.getValue();
                httpRequest.setHeader(key, value);
            }
            String requestInfo = getRequestInfo(httpRequest);
            Log.info(CLASS_NAME, "请求信息: " + requestInfo);
            String bodyInfo = null;
            HttpEntity httpEntity = null;
            if (httpRequest instanceof HttpPost) {
                httpEntity = ((HttpPost) httpRequest).getEntity();
            }
            if (httpRequest instanceof HttpDelete) {
                httpEntity = ((HttpDelete) httpRequest).getEntity();
            }
            if (httpRequest instanceof HttpPut) {
                httpEntity = ((HttpPut) httpRequest).getEntity();
            }
            if (httpEntity != null) {
                bodyInfo = EntityUtils.toString(httpEntity);
            }
            Log.info(CLASS_NAME, "请求Body信息: " + bodyInfo);
            response = httpClient.execute(httpRequest);
            responseInfo = getResponseInfo(response);
            Log.info(CLASS_NAME, "响应信息: " + responseInfo);
            String fullInfo = getFullInfo(requestInfo, bodyInfo, responseInfo);
            Log.info(CLASS_NAME, "接口请求/响应信息: " + fullInfo);
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
        return responseInfo;
    }

    private static String getRequestInfo(HttpRequestBase request) {
        StringBuilder sb = new StringBuilder("{\"url\":\"");
        String url = request.getRequestLine().getUri();
        String method = request.getRequestLine().getMethod();
        sb.append(url)
                .append("\",\"method\":\"")
                .append(method)
                .append("\",")
                .append("\"requestHeader\":{")
                .append(parseHeader(request.getAllHeaders()))
                .append("}}");
        return CommonUtil.format(sb);
    }

    private static String getResponseInfo(CloseableHttpResponse response)
            throws ParseException, IOException {
        StringBuilder sb = new StringBuilder("{\"responseCode\":");
        int responseCode = response.getStatusLine().getStatusCode();
        String responseMessage = response.getStatusLine().getReasonPhrase();
        sb.append(responseCode)
                .append(",")
                .append("\"responseMessage\":\"")
                .append(responseMessage)
                .append("\",\"responseHeader\":{")
                .append(parseHeader(response.getAllHeaders()))
                .append("},\"response\":");
        HttpEntity responseEntity = response.getEntity();
        String responseInfo = null;
        if (responseEntity != null) {
            responseInfo = EntityUtils.toString(responseEntity, "UTF-8");
            if ("".equals(responseInfo))
                responseInfo = "\"\"";
        }
        sb.append(format(responseInfo))
                .append("}");
        return CommonUtil.format(sb);
    }

    private static String getFullInfo(String requestInfo, String bodyInfo, String responseInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"requestInfo\":")
                .append(requestInfo)
                .append(",\"bodyInfo\":")
                .append(format(bodyInfo))
                .append(",\"responseInfo\":")
                .append(format(responseInfo))
                .append("}");
        return sb.toString();
    }

    private static String parseHeader(Header[] headers) {
        StringBuilder sb = new StringBuilder();
        for (Header header : headers) {
            String value = header.getValue();
            value = value.replaceAll("\"", "\\\\\"");
            sb.append("\"")
                    .append(header.getName())
                    .append("\":\"")
                    .append(value)
                    .append("\",");
        }
        return sb.toString();
    }

    private static String format(String target) {
        if (target != null) {
            if (!target.startsWith("{") && !target.startsWith("[")) {
                return "\"" + target + "\"";
            }
        }
        return target;
    }

}
