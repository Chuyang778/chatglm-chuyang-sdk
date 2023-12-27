package com.chuyang.session.defaults;

import com.alibaba.fastjson.JSON;
import com.chuyang.api.IOpenAIApi;
import com.chuyang.model.ChatCompletionRequest;
import com.chuyang.model.ChatCompletionResponse;
import com.chuyang.model.EventType;
import com.chuyang.session.Configuration;
import com.chuyang.session.OpenAiSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.reactivex.Completable;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author ChuYang
 * @version 1.0
 */
public class DefaultOpenAiSession implements OpenAiSession {

    private final Configuration configuration;

    private final EventSource.Factory factory;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
    }


    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAIApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getType()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAIApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getType()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                if (EventType.add.getType().equals(type)) {
                    dataBuffer.append(response.getData());
                } else if (EventType.finish.getType().equals(type)) {
                    future.complete(dataBuffer.toString());
                }
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });
        return future;
    }
}
