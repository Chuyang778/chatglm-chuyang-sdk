package com.chuyang.session;

import com.chuyang.model.ChatCompletionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.reactivex.Completable;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author ChuYang
 * @version 1.0
 */
public interface OpenAiSession {
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;
}
