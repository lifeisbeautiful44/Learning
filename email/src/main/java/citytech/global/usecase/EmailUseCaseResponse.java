package citytech.global.usecase;

import citytech.global.platform.usecase.UseCaseResponse;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record EmailUseCaseResponse(String data)  implements UseCaseResponse {

}
