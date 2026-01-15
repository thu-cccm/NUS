package com.maple.common.util;

import com.maple.common.model.HttpReqInfo;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientUtil {

    private HttpClientUtil() {

    }

    protected static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String ERROR_MSG = "Exception :";

    public static CloseableHttpClient getDefaultHttpClient() {
        return getCloseableHttpClient(false);
    }

    public static CloseableHttpClient getSslHttpsClient() {
        return getCloseableHttpClient(true);
    }

    private static CloseableHttpClient getCloseableHttpClient(boolean isSsl) {
        CloseableHttpClient httpClient = null;
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(180000).setConnectTimeout(180000).setSocketTimeout(180000).build();
        try {
            if (isSsl) {
                httpClient = HttpClients.custom().setSSLSocketFactory(createSslConnSocketFactory()).setDefaultRequestConfig(requestConfig).build();
            } else {
                httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
            }

        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        }
        return httpClient;
    }

    public static String sendGet(String urlStr, List<HttpReqInfo> param) {
        return sendGet(getDefaultHttpClient(), urlStr, param);
    }

    public static String sendGet(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param) {
        return sendGetContext(httpClient, urlStr, param, null);
    }

    public static String sendGetContext(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param, HttpContext httpContext) {
        String result = "";

        try {
            CloseableHttpResponse httpResponse = sendGetContextRes(httpClient, urlStr, param, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }

        return result;
    }

    public static CloseableHttpResponse sendGetContextRes(CloseableHttpClient httpClient,
                                                          String urlStr,
                                                          List<HttpReqInfo> param,
                                                          HttpContext httpContext) throws IOException {
        urlStr = generateUrlWithParam(urlStr, param);
        HttpGet httpGet = new HttpGet(urlStr);
        return httpClient.execute(httpGet, httpContext);
    }

    public static String sendJsonPost(String urlStr, String json) {
        return sendJsonPostHeader(getDefaultHttpClient(), urlStr, json, null);
    }

    public static String sendJsonPost(CloseableHttpClient httpClient, String urlStr, String json) {
        return sendJsonPostHeader(httpClient, urlStr, json, null);
    }

    public static String sendJsonPostHeader(CloseableHttpClient httpClient, String urlStr, String json, List<HttpReqInfo> header) {
        return sendBodyPostHeaderContext(httpClient, urlStr, json, header, null);
    }

    public static String sendBodyPostHeaderContext(CloseableHttpClient httpClient,
                                                   String urlStr,
                                                   String body,
                                                   List<HttpReqInfo> header,
                                                   HttpContext httpContext) {
        String result = "";
        try {
            CloseableHttpResponse httpResponse = sendBodyPostHeaderContextRes(httpClient, urlStr, body, header, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }
        return result;
    }

    public static CloseableHttpResponse sendBodyPostHeaderContextRes(CloseableHttpClient httpClient, String urlStr, String body, List<HttpReqInfo> header, HttpContext httpContext) throws IOException {
        EntityBuilder eb = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(body);
        HttpPost httpPost = new HttpPost(urlStr);
        if (header != null && !header.isEmpty()) {
            for (HttpReqInfo headerInfo : header) {
                if (!headerInfo.isHeader()) {
                    continue;
                }
                httpPost.addHeader(headerInfo.getParam(), headerInfo.getValue());
            }
        }
        httpPost.setEntity(eb.build());
        return httpClient.execute(httpPost, httpContext);
    }

    public static String sendJsonPut(String urlStr, String json) {
        return sendJsonPutHeader(getDefaultHttpClient(), urlStr, json, null);
    }

    public static String sendJsonPut(CloseableHttpClient httpClient, String urlStr, String json) {
        return sendJsonPutHeader(httpClient, urlStr, json, null);
    }

    public static String sendJsonPutHeader(CloseableHttpClient httpClient, String urlStr, String json, List<HttpReqInfo> header) {
        return sendBodyPutHeaderContext(httpClient, urlStr, json, header, null);
    }

    public static String sendBodyPutHeaderContext(CloseableHttpClient httpClient,
                                                  String urlStr,
                                                  String body,
                                                  List<HttpReqInfo> header,
                                                  HttpContext httpContext) {
        String result = "";
        try {
            CloseableHttpResponse httpResponse = sendBodyPutHeaderContextRes(httpClient, urlStr, body, header, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }
        return result;
    }

    public static CloseableHttpResponse sendBodyPutHeaderContextRes(CloseableHttpClient httpClient,
                                                                    String urlStr,
                                                                    String body, List<HttpReqInfo> header,
                                                                    HttpContext httpContext) throws IOException {
        EntityBuilder eb = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(body);
        HttpPut httpPut = new HttpPut(urlStr);
        if (header != null && !header.isEmpty()) {
            for (HttpReqInfo headerInfo : header) {
                if (!headerInfo.isHeader()) {
                    continue;
                }
                httpPut.addHeader(headerInfo.getParam(), headerInfo.getValue());
            }
        }
        httpPut.setEntity(eb.build());
        return httpClient.execute(httpPut, httpContext);
    }

    public static String sendDelete(String urlStr, List<HttpReqInfo> param) {
        return sendDelete(getDefaultHttpClient(), urlStr, param);
    }

    public static String sendDelete(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param) {
        return sendDeleteContext(httpClient, urlStr, param, null);
    }

    public static String sendDeleteContext(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param, HttpContext httpContext) {
        String result = "";

        try {
            CloseableHttpResponse httpResponse = sendDeleteContextRes(httpClient, urlStr, param, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }

        return result;
    }

    public static CloseableHttpResponse sendDeleteContextRes(CloseableHttpClient httpClient,
                                                             String urlStr,
                                                             List<HttpReqInfo> param,
                                                             HttpContext httpContext) throws IOException {
        urlStr = generateUrlWithParam(urlStr, param);
        HttpDelete httpDelete = new HttpDelete(urlStr);
        return httpClient.execute(httpDelete, httpContext);
    }

    public static String generateUrlWithParam(String url, List<HttpReqInfo> param) {
        if (param == null || param.isEmpty()) {
            return url;
        }
        List<NameValuePair> nvpList = new ArrayList<>();
        for (HttpReqInfo reqInfo : param) {
            nvpList.add(new BasicNameValuePair(reqInfo.getParam(), reqInfo.getValue()));
        }
        return String.format("%s?%s", url, URLEncodedUtils.format(nvpList, StandardCharsets.UTF_8));
    }

    private static String consumeResponse(CloseableHttpResponse httpResponse) throws IOException {
        String result = "";
        try {
            
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
        } finally {
            httpResponse.close();
        }
        return result;
    }

    private static SSLConnectionSocketFactory createSslConnSocketFactory() {
        SSLConnectionSocketFactory ssl = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            ssl = new SSLConnectionSocketFactory(sslContext, (s, sslSession) -> Boolean.TRUE);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return ssl;
    }
}
