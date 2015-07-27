package com.bionic.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viktozhu on 6/2/15.
 */
public class AdfsAuth {
    private static Logger logger = LoggerFactory.getLogger(AdfsAuth.class);

    private String URL;
    private String user;
    private String password;
    private String domain;

    private static final String HOST = "localhost";
    private static final String HEADER_LOCATION = "Location";
    private static final String ENC_UTF8 = "UTF-8";

    public AdfsAuth(String login, String password, String url, String domain){
        this.URL = url;
        this.user = login;
        this.password = password;
        this.domain = domain;
    }

    public void getCookies() throws IOException, KeyManagementException, NoSuchAlgorithmException {
        /**
         * Configure common params for requests
         */
        logger.debug("Connecting to server " + URL);
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.protocol.handle-redirects",false);

        /**
         *  Configure context with cookies
         */
        CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext cookiesContext = HttpClientContext.create();
        cookiesContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        /**
         * Configure NTLM context
         */
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new NTCredentials(user, password, HOST, domain));
        HttpClientContext ntlmContext = HttpClientContext.create();
        ntlmContext.setCredentialsProvider(credsProvider);

        /**
         * Build client
         */
        CloseableHttpClient client = HttpClients.custom()
                .disableRedirectHandling()
                .build();

        /**
         * Try Load MA Pursuit
         */
        HttpGet loadDashboardRequest = new HttpGet(URL);
        CloseableHttpResponse response = client.execute(loadDashboardRequest, cookiesContext);

        String url = "";
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
        {
            // request qauth.qm...
            url = response.getHeaders(HEADER_LOCATION)[0].getValue();
            logger.debug("Connection redirected to "+ url);
        }

        /**
         * Load auth page
         */
        HttpGet loadAuthPageRequest = new HttpGet(url);
        response = client.execute(loadAuthPageRequest);
        String content = EntityUtils.toString(response.getEntity(), ENC_UTF8);

        /**
         * Select qauth.deloitte.com provider for login
         */
        url = String.format("%s://%s%s", loadAuthPageRequest.getURI().getScheme(), loadAuthPageRequest.getURI().getHost(), getFormAction(content));
        HttpPost selectAuthProviderRequest  = new HttpPost(url);
        selectAuthProviderRequest.setEntity(new StringEntity(getPostBody(getAllInputsTags(content), false), ContentType.APPLICATION_FORM_URLENCODED));
        logger.debug("Requesting for ADFS server for login + " + url);

        response = client.execute(selectAuthProviderRequest);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
        {
            // get redirect url
            url = String.format("%s://%s%s",
                    selectAuthProviderRequest.getURI().getScheme(),
                    selectAuthProviderRequest.getURI().getHost(),
                    response.getHeaders(HEADER_LOCATION)[0].getValue());
        }

        /**
         * Login to qauth.deloitte.com with NTLM
         */
        HttpGet loginNtlmRequest = new HttpGet(url);
        response = client.execute(loginNtlmRequest, ntlmContext);
        content = EntityUtils.toString(response.getEntity(), ENC_UTF8);
        logger.debug("Login to NTLM " + content);
        /**
         * Get FedAuth cookies request
         */
        HttpPost getFedAuthRequest = new HttpPost(getFormAction(content));
        getFedAuthRequest.setEntity(new StringEntity(getPostBody(getAllInputsTags(content), true), ContentType.APPLICATION_FORM_URLENCODED));

        response = client.execute(getFedAuthRequest, cookiesContext);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)
        {
            logger.debug("FedAuth cookie granted " + cookiesContext);
            String BLUE_STRIPE_PVN = cookiesContext.getCookieStore().getCookies().get(0).getValue();
            String FED_AUTH = cookiesContext.getCookieStore().getCookies().get(1).getValue();
            /**
             * Try load ma, if hang-up then authorization failed
             */
//            HttpGet loadMARequest = new HttpGet(URL);
//            response = client.execute(loadMARequest, cookiesContext);
//            content = EntityUtils.toString(response.getEntity(), ENC_UTF8);
//            System.out.print(content);
        }

        client.close();
    }

    /**
     *
     * @param inputTags
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getPostBody( Map<String, String> inputTags, boolean useHtmlDecoding) throws UnsupportedEncodingException {

        String body = "";

        for (Map.Entry<String, String> e : inputTags.entrySet()) {

            String key = e.getKey();
            if (StringUtil.isBlank(key))
                continue;

            String val = useHtmlDecoding ?  StringEscapeUtils.unescapeHtml4(e.getValue()) : e.getValue();

            body += String.format("%s%s=%s",
                    StringUtil.isBlank(body) ? "" : "&",
                    URLEncoder.encode(key, ENC_UTF8),
                    URLEncoder.encode(val, ENC_UTF8));
        }

        return body;
    }

    /**
     *
     * @param content
     * @return
     */
    public static Map<String, String> getAllInputsTags(String content) {
        Map<String, String> map = new HashMap<String, String>();

        Document doc = Jsoup.parse(content);
        Elements inputs = doc.select("input,select");

        for (Element input : inputs)
        {
            String name = input.attributes().get("name");
            String value = input.attributes().get("value");

            map.put(name, value);
        }

        return map;
    }

    /**
     *
     * @param content
     * @return
     */
    public static String getFormAction(String content)
    {
        Document doc = Jsoup.parse(content);
        Element input = doc.select("form").first();

        return input.attributes().get("action");
    }
}
