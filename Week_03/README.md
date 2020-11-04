# 1、搭建网关整体框架
定义一个抽象基类OutBoundHandler，子类继承，实现了OkHttp和HttpClient两种方式

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