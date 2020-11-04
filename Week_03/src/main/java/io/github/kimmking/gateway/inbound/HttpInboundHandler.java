package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.impl.HttpRequestFilterImpl;
import io.github.kimmking.gateway.outbound.OutBoundHandler;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private HttpRequestFilter httpRequestFilter = new HttpRequestFilterImpl();
    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    //String proxyServer = System.getProperty("proxyServer","http://localhost:8088");
    //String proxyPort = System.getProperty("proxyPort","8888");
    private final String proxyServer;
    private OutBoundHandler handler;
    
    public HttpInboundHandler(String proxyServer) {
        logger.info("aaa{}",proxyServer);
        this.proxyServer = proxyServer;
        //可以通过以下两种方式httpclient和okHttp
        handler = new OkhttpOutboundHandler(this.proxyServer);
//        handler = new HttpClientHttpOutboundHandler(this.proxyServer);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            httpRequestFilter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
