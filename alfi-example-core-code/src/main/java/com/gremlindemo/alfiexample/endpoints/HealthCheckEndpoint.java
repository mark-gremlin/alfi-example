package com.gremlindemo.alfiexample.endpoints;

import com.gremlin.GremlinService;
import com.gremlin.TrafficCoordinates;
import com.nike.riposte.server.http.RequestInfo;
import com.nike.riposte.server.http.ResponseInfo;
import com.nike.riposte.server.http.StandardEndpoint;
import com.nike.riposte.util.Matcher;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import io.netty.channel.ChannelHandlerContext;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Dummy health check endpoint. This will immediately respond with a 200 HTTP status code. It will let you know when
 * your machine has fallen over but not much else (which may be sufficient depending on your use case).
 *
 * @author Nic Munroe
 */
public class HealthCheckEndpoint extends StandardEndpoint<Void, Void> {

    private static final Matcher MATCHER = Matcher.match("/healthcheck");

    private final GremlinService gremlinService;

    @Inject
    public HealthCheckEndpoint(@Named("gremlinService") GremlinService gremlinService) {

        this.gremlinService = gremlinService;
    }

    @Override
    public @NotNull CompletableFuture<ResponseInfo<Void>> execute(
        @NotNull RequestInfo<Void> request,
        @NotNull Executor longRunningTaskExecutor,
        @NotNull ChannelHandlerContext ctx
    ) {
        gremlinService.applyImpact(new TrafficCoordinates.Builder()
                .withType(this.getClass().getSimpleName()).withField("method", "execute")
                .build());
        return CompletableFuture.completedFuture(ResponseInfo.<Void>newBuilder().withHttpStatusCode(200).build());
    }

    @Override
    public @NotNull Matcher requestMatcher() {
        return MATCHER;
    }
}
