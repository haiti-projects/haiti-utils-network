package dev.struchkov.haiti.utils.network;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.struchkov.haiti.utils.Assert;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Утилитарный класс для работы с web.
 *
 * @author upagge 30.09.2020
 */
public class HttpParse {

    private static final Logger log = LoggerFactory.getLogger(HttpParse.class);

    public static final String X_API_TOKEN = "x-api-token";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    public static final HttpHeader ACCEPT = HttpHeader.of("Accept", "text/html,application/xhtml+xml,application/json");

    private static final ObjectMapper objectMapper;

    private final Request.Builder requestBuilder = new Request.Builder();
    private final HttpUrl.Builder httpUrlBuilder;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public HttpParse(String url) {
        Assert.isNotNull(url);
        httpUrlBuilder = HttpUrl.parse(url).newBuilder();
    }

    public static HttpParse request(String url) {
        Assert.isNotNull(url);
        return new HttpParse(url);
    }

    public HttpParse header(String name, String value) {
        Assert.isNotNull(name);
        if (value != null) {
            requestBuilder.header(name, value);
        }
        return this;
    }

    public HttpParse header(HttpHeader header) {
        Assert.isNotNull(header);
        requestBuilder.header(header.getName(), header.getValue());
        return this;
    }

    public HttpParse getParameter(String name, String value) {
        Assert.isNotNull(name);
        if (value != null) {
            httpUrlBuilder.addQueryParameter(name, value);
        }
        return this;
    }

    public <T> Optional<T> execute(Class<T> classOfT) {
        Assert.isNotNull(classOfT);
        final Request request = requestBuilder.url(httpUrlBuilder.build()).build();
        try (final Response execute = client.newCall(request).execute()) {
            if (execute.isSuccessful() && execute.body() != null) {
                final String string = execute.body().string();
                return Optional.ofNullable(objectMapper.readValue(string, classOfT));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    public <T> List<T> executeList(Class<T> classOfT) {
        Assert.isNotNull(classOfT);
        final Request request = requestBuilder.url(httpUrlBuilder.build()).build();
        try (final Response execute = client.newCall(request).execute()) {
            if (execute.isSuccessful() && execute.body() != null) {
                final List<T> list = objectMapper.readValue(execute.body().string(), objectMapper.getTypeFactory().constructCollectionType(List.class, classOfT));
                return (list == null || list.isEmpty()) ? Collections.emptyList() : list;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

}