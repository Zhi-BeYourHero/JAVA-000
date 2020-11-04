# 1、搭建网关整体框架
定义一个抽象基类OutBoundHandler，子类继承，实现了OkHttp和HttpClient两种方式
```java
public abstract class OutBoundHandler {
    protected static final int CORES = Runtime.getRuntime().availableProcessors() * 2;
    private static final long KEEP_ALIVE_TIME = 1000;
    private static final int QUEUE_SIZE = 2048;
    private final RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
    protected final ExecutorService proxyService = new ThreadPoolExecutor(CORES, CORES,
            KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(QUEUE_SIZE),
            new NamedThreadFactory("proxyService"), handler);
    protected String backendUrl;
    public OutBoundHandler(String backendUrl){
        this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        final String url = this.backendUrl + fullRequest.uri();
        proxyService.submit(()->fetchGet(fullRequest, ctx, url));
    }

    protected abstract void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url);

    protected void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final byte[] body, final int contentLen){
        FullHttpResponse response = null;
        try {
            System.out.println(new String(body));
            System.out.println(body.length);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", contentLen);
        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }

    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
```
```java
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
```
```java
public class HttpClientHttpOutboundHandler extends OutBoundHandler {
    
    private CloseableHttpAsyncClient httpclient;

    //String proxyServer = System.getProperty("proxyServer","http://localhost:8088");
    public HttpClientHttpOutboundHandler(String backendUrl){
        super(backendUrl);

        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(CORES)
                .setRcvBufSize(32 * 1024)
                .build();
        
        httpclient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpclient.start();
    }
    
    protected void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        final HttpGet httpGet = new HttpGet(url);
        //httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse endpointResponse) {
                try {
                    byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
                    int contentLen = Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue());
                    handleResponse(inbound, ctx, body, contentLen);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    
                }
            }
            
            @Override
            public void failed(final Exception ex) {
                httpGet.abort();
                ex.printStackTrace();
            }
            
            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }
}

```
# 2、实现过滤器
```java
public class HttpRequestFilterImpl implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("nio", "ChengYu Ye");
    }
}
```
简单设置

# 3、随机负载均衡
```java
public class HttpEndpointRouterRandom implements HttpEndpointRouter {
    Random random = new Random();
    @Override
    public String route(List<String> endpoints) {
        return endpoints.get(random.nextInt(endpoints.size()));
    }
}
```