package com.chuyang.test;

import com.alibaba.fastjson.JSON;
import com.chuyang.model.*;
import com.chuyang.session.Configuration;
import com.chuyang.session.OpenAiSession;
import com.chuyang.session.OpenAiSessionFactory;
import com.chuyang.session.defaults.DefaultOpenAiSessionFactory;
import com.chuyang.utils.BearerTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author ChuYang
 * @version 1.0
 */
@Slf4j
public class ApiTest {

    private OpenAiSession openAiSession;

    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("3d8199d9f2380bf5cfa8324a82ea534b.86xBtGAt0zgd37Ng");
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    @Test
    public void test_completions() throws JsonProcessingException, InterruptedException {
        // 入参；模型、请求信息
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
        request.setIncremental(false);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            private static final long serialVersionUID = -7988151926241837899L;

            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getRole())
                        .content("1+2")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getRole())
                        .content("Okay")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.system.getRole())
                        .content("1+1=2")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getRole())
                        .content("Okay")
                        .build());

                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getRole())
                        .content("1+2")
                        .build());
            }
        });

        // 请求
        openAiSession.completions(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                log.info("测试结果 onEvent：{}", response.getData());
                // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if (EventType.finish.getType().equals(type)) {
                    ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
                    log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info("对话完成");
            }
        });

        // 等待
        new CountDownLatch(1).await();
    }

    @Test
    public void test_completions_future() throws Exception{
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_LITE);
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>(){
            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getRole())
                        .content("用java实现一个迪杰斯特拉算法")
                        .build());
            }
        });
        CompletableFuture<String> future = openAiSession.completions(request);
        String response = future.get();
        System.out.println(response);
    }

    @Test
    public void test_completions_sync() throws IOException, InterruptedException {
        // 入参；模型、请求信息
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_TURBO); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            private static final long serialVersionUID = -7988151926241837899L;

            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getRole())
                        .content("写个java冒泡排序")
                        .build());
            }
        });

        ChatCompletionSyncResponse response = openAiSession.completionsSync(request);

        log.info("测试结果：{}", response);
    }

    @Test
    public void test_curl() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://open.bigmodel.cn/");
        configuration.setApiSecretKey("4e087e4135306ef4a676f0cce3cee560.sgP2DUs******");

        // 2. 获取Token
        String token = BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret());
        log.info("1. 在智谱Ai官网，申请 ApiSeretKey 配置到此测试类中，替换 setApiSecretKey 值。 https://open.bigmodel.cn/usercenter/apikeys");
        log.info("2. 运行 test_curl 获取 token：{}", token);
        log.info("3. 将获得的 token 值，复制到 curl.sh 中，填写到 Authorization: Bearer 后面");
        log.info("4. 执行完步骤3以后，可以复制直接运行 curl.sh 文件，或者复制 curl.sh 文件内容到控制台/终端/ApiPost中运行");
    }
}
