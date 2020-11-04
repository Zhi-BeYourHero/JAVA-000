package io.github.kimmking.gateway.router.impl;

import io.github.kimmking.gateway.router.HttpEndpointRouter;

import java.util.List;
import java.util.Random;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-04 10:24
 */
public class HttpEndpointRouterRandom implements HttpEndpointRouter {
    Random random = new Random();
    @Override
    public String route(List<String> endpoints) {
        return endpoints.get(random.nextInt(endpoints.size()));
    }
}