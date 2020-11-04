package io.github.kimmking.gateway.outbound.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpClientOutboundHandler  extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(NettyHttpClientOutboundHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        
    }

    /**
     * 收到服务器响应的消息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String uri = fullRequest.uri();
            //logger.info("接收到的请求url为{}", uri);

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}