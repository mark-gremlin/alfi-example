package com.gremlindemo.alfiexample.endpoints;

import com.gremlin.ApplicationCoordinates;
import com.gremlin.GremlinCoordinatesProvider;
import com.gremlin.GremlinService;
import com.gremlin.GremlinServiceFactory;
import com.nike.riposte.server.http.RequestInfo;
import com.nike.riposte.server.http.ResponseInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import io.netty.channel.ChannelHandlerContext;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Verifies the functionality of {@link HealthCheckEndpoint}
 *
 * @author Nic Munroe
 */
public class HealthCheckEndpointTest {

    private HealthCheckEndpoint healthCheckEndpoint;

    @Test
    public void healthCheckEndpoint_should_match_all_http_methods() {
        // expect
        assertThat(healthCheckEndpoint.requestMatcher().isMatchAllMethods()).isTrue();
    }

    @Test
    public void healthCheckEndpoint_should_always_return_http_status_code_200() {
        // given
        @SuppressWarnings("unchecked")
        RequestInfo<Void> requestMock = mock(RequestInfo.class);
        Executor executorMock = mock(Executor.class);
        ChannelHandlerContext ctxMock = mock(ChannelHandlerContext.class);

        // when
        CompletableFuture<ResponseInfo<Void>> responseFuture = healthCheckEndpoint.execute(
            requestMock, executorMock, ctxMock
        );
        ResponseInfo<Void> responseInfo = responseFuture.join();

        // then
        assertThat(responseInfo.getHttpStatusCode()).isEqualTo(200);
    }

    @Before
    public void setup() {


        healthCheckEndpoint = new HealthCheckEndpoint(gremlinService());
    }

    private GremlinService gremlinService() {
        return gremlinServiceFactory(gremlinCoordinatesProvider()).getGremlinService();
    }

    private GremlinServiceFactory gremlinServiceFactory(GremlinCoordinatesProvider gremlinCoordinatesProvider) {
        return new GremlinServiceFactory(gremlinCoordinatesProvider);
    }

    private GremlinCoordinatesProvider gremlinCoordinatesProvider() {
        return new GremlinCoordinatesProvider() {
            @Override
            public ApplicationCoordinates initializeApplicationCoordinates() {
                return new ApplicationCoordinates.Builder()
                        .withType("Riposte")
                        .build();
            }
        };
    }

}