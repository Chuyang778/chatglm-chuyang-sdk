package com.chuyang.api;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import io.reactivex.Single;
import retrofit2.http.POST;

/**
 * @author ChuYang
 * @version 1.0
 */
public interface IOpenAIApi {
    String v3_completions = "api/paas/v3/model-api/{model}/sse-invoke";

}
