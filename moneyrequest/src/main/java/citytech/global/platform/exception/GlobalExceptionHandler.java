package citytech.global.platform.exception;



import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {Exception.class, ExceptionHandler.class})
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {

        String code = "0";
        String message = exception.getMessage();
        String jsonResponse = String.format("{\"code\": %s, \"message\": \"%s\"}", code, message);
        return HttpResponse.badRequest().body(jsonResponse);    }
}