package citytech.global.platform.exception;



import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {MoneyRequestException.class, ExceptionHandler.class})
public class MoneyRequestExceptionHandler implements ExceptionHandler<MoneyRequestException, HttpResponse<?>> {
    @Override
    public HttpResponse<?> handle(HttpRequest request, MoneyRequestException exception) {
        String code = exception.getCode();
        String message = exception.getMessage();
        String data = exception.getData();
        String jsonResponse = String.format("{\"code\": %s, \"message\": \"%s\", \"data\": \"%s\"}", code, message, data);
        return HttpResponse.badRequest().body(jsonResponse);
    }
}
