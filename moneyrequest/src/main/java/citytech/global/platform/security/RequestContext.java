package citytech.global.platform.security;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record RequestContext(String subject,
                             String userType,
                             long userID
) {
}
