package com.demon4u.blog.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * HTTP访问的工具类
 */
public class HttpClientUtil {

    private HttpClientUtil() {

    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 错误提示
     */
    private static final String POST_REQDATA_ERRMSG = "HttpClient请求数据出错:";
    /**
     * logger
     */

    private static final String MINETYPE_JSON = "application/json";

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 链接池
     */
    private static PoolingClientConnectionManager connManager = null;
    private static HttpParams params = null;

    private static HttpClient httpClient = null;

    private static int errorCount = 0;

    /**
     * 超过最大错误数会重新初始化
     */
    private static final int ERROR_MAX_COUNT = 100;

    /**
     * 初始化资源的线程锁
     */
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    static {
        httpClient = init();
    }

    /**
     * 发送http请求
     *
     * @param url 请求地址
     * @param data 请求报文
     * @return 返回报文
     */
    public static String postData(String url, String data) {
        return postData(url, data, null, null, null, null, null);
    }

    /**
     * 使用代理post
     *
     * @param url
     * @param formData
     * @param proxy
     * @return
     */
    public static String postFormData(String url, Map<String, String> formData, HttpHost proxy) throws IOException {
        return postFormData(url, formData, proxy, null);
    }

    public static String postFormData(String url, Map<String, String> formData, HttpHost proxy,
                                      Map<String, String> headers) throws IOException {
        HttpPost httppost = new HttpPost(url);

        if (null != proxy) {
            httppost.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        if (formData != null) {
            List<NameValuePair> formParams = new ArrayList<>();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, DEFAULT_CHARSET);
            httppost.setEntity(entity);
        }
        if (null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse httpResponse = httpClient.execute(httppost);
        return readResponse(httpResponse.getEntity(), DEFAULT_CHARSET);

    }

    /**
     * 发送http请求，请求的mineType为application/json
     *
     * @param url 请求地址
     * @param data 请求报文
     * @return 返回报文
     */
    public static String postJsonData(String url, String data) {
        return postData(url, data, null, null, null, null, MINETYPE_JSON);
    }

    /**
     * 发送请求数据
     *
     * @param url 请求地址
     * @param data 请求数据
     * @param formData form数据
     * @param multipartformData multipartform数据
     * @param headers 请求头
     * @param cookie cookie
     * @param mineType mineType
     * @return 返回报文
     */
    public static String postData(String url, String data, Map<String, String> formData,
                                  List<FormBodyPart> multipartformData, Map<String, String> headers, String cookie, String mineType) {
        try {
            if (errorCount > ERROR_MAX_COUNT) {
                httpClient = init();
            }
            // 请求开始时间
            // 发送请求
            lock.readLock().lock();
            HttpResponse response = postHttp(httpClient, url, data, formData, multipartformData, headers, cookie,
                    mineType, null);
            String result = readResponse(response.getEntity(), DEFAULT_CHARSET);
            return result;
        } catch (Exception e) {
            errorCount++;
            LOGGER.error(POST_REQDATA_ERRMSG, e);
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    /**
     * 通过get方式获取资源
     *
     * @param url
     * @param cookie
     * @param userAgent
     * @return
     */
    public static String getData(String url, String cookie, String userAgent) {
        return getData(url, cookie, userAgent, DEFAULT_CHARSET);
    }

    /**
     * 通过get方式获取资源
     *
     * @param url
     * @param cookie
     * @param userAgent
     * @return
     */
    public static String getData(String url, String cookie, String userAgent, String charsetParam) {

        String charset = StringUtils.isEmpty(charsetParam) ? DEFAULT_CHARSET : charsetParam;

        // 新建get获取类型
        HttpGet httpGet = new HttpGet(url);
        // 判断cookie是否为空，并进行设置
        if (StringUtils.isNotBlank(cookie)) {
            httpGet.addHeader("Cookie", cookie);
        }
        // 判断userAgent是否为空，并进行设置
        if (StringUtils.isNotBlank(userAgent)) {
            httpGet.addHeader("User-Agent", userAgent);
        }
        // 默认response 内容
        HttpResponse respone = null;
        // 默认返回值内容
        String res = null;
        try {
            if (errorCount > ERROR_MAX_COUNT) {
                httpClient = init();
            }
            lock.readLock().lock();
            // 执行请求
            respone = httpClient.execute(httpGet);
            // 读取内容
            res = readResponse(respone.getEntity(), charset);
        } catch (Exception e) {
            LOGGER.info("执行请求读取内容出现异常{}", e);

            // 出现异常返回空
            return null;
        } finally {
            lock.readLock().unlock();
        }
        return res;
    }

    /**
     * 初始化
     */
    private static HttpClient init() {
        try {
            LOGGER.info("HttpClient线程池初始化。。。");
            lock.writeLock().lock();
            if (httpClient != null && errorCount < ERROR_MAX_COUNT) {
                return httpClient;
            }
            if (connManager != null) {
                connManager.shutdown();
            }

            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                    // Do nothing because Override method.
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {
                    // Do nothing because Override method.
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // 设置访问协议
            SchemeRegistry schreg = new SchemeRegistry();
            schreg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            schreg.register(new Scheme("https", 443, ssf));

            connManager = new PoolingClientConnectionManager(schreg);
            connManager.setDefaultMaxPerRoute(2000); // 每个主机的最大并行链接数
            connManager.setMaxTotal(10000); // 客户端总并行链接最大数

            // 设置组件参数, HTTP协议的版本,1.1/1.0/0.9
            params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
            // HttpProtocolParams.setUseExpectContinue(params, true)

            // 设置连接超时时间
            int requsetTimeout = 20 * 1000; // 设置请求超时5秒钟
            int soTimeout = 20 * 1000; // 设置等待数据超时时间10秒钟
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, requsetTimeout);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
            httpClient = new DefaultHttpClient(connManager, params);
        } catch (Exception e) {
            LOGGER.error("init error  " + e);
            httpClient = new DefaultHttpClient(params);
        } finally {
            lock.writeLock().unlock();
        }
        errorCount = 0;
        LOGGER.info("HttpClient线程池初始化完成。。。");
        return httpClient;
    }

    /**
     * 读取返回
     *
     * @param resEntity
     * @return String 返回
     */
    private static String readResponse(HttpEntity resEntity, String charset) {
        StringBuilder res = new StringBuilder();
        BufferedReader reader = null;
        try {
            if (resEntity == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(resEntity.getContent(), charset));
            String line = null;

            while ((line = reader.readLine()) != null) {
                res.append(line);
            }

        } catch (Exception e) {
            LOGGER.info("创建读取流出现异常{}", e);
        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                LOGGER.info("关闭流出现异常{}", e);
            }

        }
        LOGGER.debug(res.toString());
        return res.toString();
    }

    /**
     * 获取响应
     *
     * @param httpclient
     * @param url
     * @param data
     * @param formData
     * @param headers
     * @param cookie
     * @param localContext
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    @SuppressWarnings("deprecation")
    private static HttpResponse postHttp(HttpClient httpclient, String url, String data, Map<String, String> formData,
                                         List<FormBodyPart> multipartFormData, Map<String, String> headers, String cookie, String mineType,
                                         HttpContext localContext) throws ClientProtocolException, IOException { // NOSONAR
        HttpPost httppost = new HttpPost(url);
        if (data != null) {
            StringEntity reqEntity;
            if (mineType == null) {
                reqEntity = new StringEntity(data, "text/html", "utf-8");
            } else {
                reqEntity = new StringEntity(data, mineType, "utf-8");
            }
            httppost.setEntity(reqEntity);
        } else if (formData != null) {
            List<NameValuePair> formparams = new ArrayList<>();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "utf-8");
            httppost.setEntity(entity);
        } else if (multipartFormData != null) {
            MultipartEntity entity = new MultipartEntity();
            for (FormBodyPart formBodyPart : multipartFormData) {
                entity.addPart(formBodyPart);
            }
            httppost.setEntity(entity);
        }
        if (null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httppost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (null != cookie) {
            httppost.addHeader("Cookie", cookie);
        }
        return httpclient.execute(httppost, localContext);

    }

}

