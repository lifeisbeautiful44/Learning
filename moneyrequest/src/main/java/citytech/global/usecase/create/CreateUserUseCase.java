package citytech.global.usecase.create;

import citytech.global.converter.UserConverter;
import citytech.global.platform.email.EmailHandler;
import citytech.global.platform.exception.MoneyRequestException;
import citytech.global.platform.exception.MoneyRequestExceptionType;
import citytech.global.platform.usecase.UseCase;
import citytech.global.platform.utils.PasswordGenerator;
import citytech.global.repository.User;
import citytech.global.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateUserUseCase implements UseCase<CreateUserRequest, CreateUserResponse> {
    private UserRepository userRepository;

    @Inject
    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<CreateUserResponse> execute(CreateUserRequest request) throws IOException, URISyntaxException, InterruptedException {

        validateRequest(request);
        User entity = UserConverter.toEntity(request);
        entity.setPassword(PasswordGenerator.generateRandomPassword());
        User userEntity = userRepository.save(entity);
        setEmailConfigurationForSignup(userEntity);
        var response = UserConverter.toResponse();

        return Optional.of(response);
    }

    private void validateRequest(CreateUserRequest request) {
        Pattern emailPattern = Pattern.compile(System.getenv("REGEX_PATTERN"));
        Matcher emailMatcher = emailPattern.matcher(request.email());
        if (!emailMatcher.matches()) {
            throw new MoneyRequestException(MoneyRequestExceptionType.INVALID_EMAIL_FORMAT);
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new MoneyRequestException(MoneyRequestExceptionType.EMAIL_ALREADY_EXIST);
        }
    }

    private void generateEmailRequest(EmailHandler emailHandler) throws IOException, URISyntaxException, InterruptedException {
        String json = new ObjectMapper().writeValueAsString(emailHandler);
        //Using environment variable
        String serviceHost = System.getenv("SERVICE_HOST");

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(serviceHost))
                .header("Content-Type", "application/json") // Set the content type header
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private void setEmailConfigurationForSignup(User userEntity) throws IOException, URISyntaxException, InterruptedException {
        EmailHandler sendEmail = new EmailHandler(userEntity.getEmail(), userEntity.getPassword());
        generateEmailRequest(sendEmail);
    }


}
