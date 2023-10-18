package citytech.global.usecase.create;

import citytech.global.platform.usecase.UseCaseResponse;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record CreateUserResponse(
        String data
        ) implements UseCaseResponse {
}

