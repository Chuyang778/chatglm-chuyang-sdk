package com.chuyang.session.defaults;

import com.chuyang.api.IOpenAIApi;
import com.chuyang.model.ChatCompletionRequest;
import com.chuyang.session.Configuration;
import com.chuyang.session.OpenAiSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * @author ChuYang
 * @version 1.0
 */
public class DefaultOpenAiSession implements OpenAiSession {

    private final Configuration configuration;

    private final EventSource.Factory factory;

    private IOpenAIApi openAIApi;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
        this.openAIApi = configuration.getIOpenAIApi();
    }


    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAIApi.v3_completions).replace("{model}", chatCompletionRequest.getModel().getType()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        return factory.newEventSource(request, eventSourceListener);
    }
}
