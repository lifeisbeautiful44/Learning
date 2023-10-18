package citytech.global.resource;


import citytech.global.platform.converter.EmailConverter;
import citytech.global.repository.EmailRepository;
import citytech.global.resource.payload.EmailDetailsPayload;
import citytech.global.usecase.EmailUseCase;
import citytech.global.usecase.EmailUseCaseRequest;
import citytech.global.usecase.EmailUseCaseResponse;
import freemarker.template.TemplateException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Controller("/email/")
public class EmailController {

    private EmailUseCase emailUseCase;
    private EmailRepository testRepository;

   @Inject
    public EmailController(EmailUseCase emailUseCase, EmailRepository testRepository) {
       this.testRepository = testRepository;
        this.emailUseCase = emailUseCase;
    }

    @Post("verification")
    public HttpResponse<EmailUseCaseResponse> sendMail(@Body EmailDetailsPayload userDetailPayload) throws TemplateException, IOException, SQLException {

      EmailUseCaseRequest request =  EmailConverter.toRequest(userDetailPayload);
       Optional<EmailUseCaseResponse> response  =  emailUseCase.execute(request);
       return HttpResponse.ok().body(response.get());
    }


    @Get("/test/sql")
    public void hitDatabase() throws SQLException {
       // testRepository.saveUser();


    }



}
