package com.chuyang.interceptor;

import com.chuyang.session.Configuration;
import com.chuyang.utils.BearerTokenUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


/**
 * @author ChuYang
 * @version 1.0
 */
public class OpenAIHTTPInterceptor implements Interceptor {
    /**
     * okhttp3下的拦截器
     */

    private final Configuration configuration;

    public OpenAIHTTPInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .url(original.url())
                .header("Authorization", "Bearer " + BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                .header("Content-Type", Configuration.JSON_CONTENT_TYPE)
                .header("User-Agent", Configuration.DEFAULT_USER_AGENT)
                .header("Accept", null != original.header("Accept") ? original.header("Accept") : Configuration.SSE_CONTENT_TYPE)
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
