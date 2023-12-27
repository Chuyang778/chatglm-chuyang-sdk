package com.chuyang.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import okhttp3.Interceptor;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

/**
 * @author ChuYang
 * @version 1.0
 */
@Data
public class ChatCompletionSyncResponse {
    private Integer code;
    private String msg;
    private Boolean success;

    private ChatGLMData data;

    @Data
    public static class ChatGLMData {
        private List<Choice> choices;
        private String task_status;
        private Usage usage;
        private String task_id;
        private String request_id;
    }
    @Data
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }

    @Data
    public static class Choice{
        private String role;
        private String content;
    }

}
