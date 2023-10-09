package citytech.global.usecase.login;

import citytech.global.platform.usecase.UseCaseResponse;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
@Introspected
@Serdeable
public record LoginResponse(
        String message,
        String token
) implements UseCaseResponse {
}
