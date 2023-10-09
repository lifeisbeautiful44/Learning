package citytech.global.usecase.create;

import citytech.global.platform.usecase.UseCaseResponse;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
@Introspected
@Serdeable
public record CreateUserResponse(
 String code,
 String message,
 String data
) implements UseCaseResponse {
}

