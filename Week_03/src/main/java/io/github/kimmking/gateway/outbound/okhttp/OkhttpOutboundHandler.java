package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.outbound.OutBoundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OkhttpOutboundHandler extends OutBoundHandler {
    private OkHttpClient okHttpClient;

    public OkhttpOutboundHandler(String backendUrl) {
        super(backendUrl);
        okHttpClient = new OkHttpClient();
    }

    @Override
    protected void fetchGet(FullHttpRequest inbound, ChannelHandlerContext ctx, String url) {
        System.out.println(url);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected Code："+response);
                    byte[] bytes = body.bytes();
                    int contentLen = (int)body.contentLength();
                    handleResponse(inbound, ctx, bytes, contentLen);
                }
            }
        });
    }
}
