package citytech.global.resource;


import citytech.global.resource.payload.EmailDetailsPayload;
import citytech.global.platform.EmailConfiguration;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller("/email/")
public class EmailController {

    @Post("/verification")
    public void sendMail(@Body EmailDetailsPayload userDetailPayload)
    {
        EmailConfiguration.sendMail(userDetailPayload);
        System.out.println("Everything successfully done.");
    }





}
