package citytech.global.usecase.login;

import citytech.global.platform.usecase.UseCaseRequest;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record
LoginRequest(
        String email,
        String password
) implements UseCaseRequest {
}
