package citytech.global.resource.payload;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public record EmailDetailsPayload (String userName, String password){

}
