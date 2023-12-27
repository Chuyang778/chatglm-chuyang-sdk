package com.chuyang.api;

import com.chuyang.model.ChatCompletionRequest;
import com.chuyang.model.ChatCompletionResponse;
import com.chuyang.model.ChatCompletionSyncResponse;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author ChuYang
 * @version 1.0
 */
public interface IOpenAIApi {

    String v3_completions = "api/paas/v3/model-api/{model}/sse-invoke";
    String v3_completions_sync = "api/paas/v3/model-api/{model}/invoke";

    @POST(v3_completions)
    Single<ChatCompletionResponse> completions(@Path("model") String model, @Body ChatCompletionRequest chatCompletionRequest);

    @POST(v3_completions_sync)
    Single<ChatCompletionSyncResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);

}
