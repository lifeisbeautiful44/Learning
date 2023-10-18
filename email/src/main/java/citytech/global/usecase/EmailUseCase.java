package citytech.global.usecase;

import citytech.global.platform.converter.EmailConverter;
import citytech.global.platform.email.EmailConfiguration;
import citytech.global.platform.email.EmailDetails;
import citytech.global.platform.exception.CustomResponseException;
import citytech.global.platform.exception.ExceptionType;
import citytech.global.platform.usecase.UseCase;
import citytech.global.repository.EmailRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EmailUseCase implements UseCase<EmailUseCaseRequest,EmailUseCaseResponse> {


    private final EmailRepository emailRepository;

    @Inject
    public EmailUseCase(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public Optional<EmailUseCaseResponse> execute(EmailUseCaseRequest request) throws SQLException, TemplateException, IOException {

        String template = generateEmailTemplate(request);

        Optional<String> email =  emailRepository.findEmail();

        if(email.isPresent())
        {
            EmailDetails emailInformation = EmailConverter.toEmailDetails(request,email.get(),template);
            EmailConfiguration.sendMail(emailInformation);
            return  Optional.of(new EmailUseCaseResponse("Success"));
        }

        throw new CustomResponseException(ExceptionType.EMAIL_SEND_FAILED);

    }

    private String generateEmailTemplate(EmailUseCaseRequest request) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(this.getClass(), "/views");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template template = cfg.getTemplate("welcome.ftl");

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("userEmail", request.userName());
        dataModel.put("userPassword", request.password());

        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);

        return writer.toString();
    }




}
