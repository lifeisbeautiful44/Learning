package citytech.global.platform.security;

import citytech.global.platform.restapiresponse.RestResponse;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.reactivex.Flowable;
import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@Filter("/api/v1")
public class SecurityFilter implements HttpServerFilter {
    private final String TOKEN = "X-XSRF-TOKEN";
    private final SecurityUtils securityUtils;
    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class.getName());

    @Inject
    public SecurityFilter(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        try {
            if (request.getMethod() == HttpMethod.OPTIONS)
                return Flowable.just(HttpResponse.ok());

            var token = request.getHeaders().get(TOKEN);

            if (request.getPath().contains("/create")) {
                return chain.proceed(request);
            }
            if (request.getPath().contains("/login")) {
                return chain.proceed(request);
            }
            if (request.getPath().contains("/conusme/restclient")) {
                return chain.proceed(request);
            }

            if (Objects.isNull(token) || token.isEmpty()) {
                throw new IllegalArgumentException("Security Token is missing");
            }

            RequestContext requestContext = securityUtils.parseTokenAndGetContext(token);
            logger.info("REQUESTED BY :: " + requestContext.subject());

            return Flowable.just(true)
                    .doOnRequest(t -> {
                        ContextHolder.set(requestContext);
                    })
                    .switchMap(aBoolean -> chain.proceed(request))
                    .onErrorReturn(throwable -> {
                        throwable.printStackTrace();
                        logger.info("::: ERROR IN CHAIN PROCESS :::");
                        throw new IllegalArgumentException("Security interceptor Exception");
                    });
        } catch (Exception e) {

            return Flowable.just(HttpResponse.badRequest(RestResponse.error("EX001", e.getMessage())));
        }

    }
}

