package citytech.global.platform.converter;

import citytech.global.platform.email.EmailDetails;
import citytech.global.resource.payload.EmailDetailsPayload;
import citytech.global.usecase.EmailUseCaseRequest;

public class EmailConverter {
    public static EmailUseCaseRequest toRequest(EmailDetailsPayload payload) {


        return new EmailUseCaseRequest(
                payload.userName(),
                payload.password()
        );
    }

    public static EmailDetails toEmailDetails(EmailUseCaseRequest useCaseRequest, String email, String template) {
        return new EmailDetails(
                useCaseRequest.userName(),
                email, "Login Details",
                template

        );
    }


}
