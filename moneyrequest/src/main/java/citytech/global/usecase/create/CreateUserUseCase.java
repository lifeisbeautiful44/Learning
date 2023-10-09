package citytech.global.usecase.create;

import citytech.global.converter.UserConverter;
import citytech.global.platform.email.EmailHandler;
import citytech.global.platform.exception.CustomResponseException;

import citytech.global.platform.usecase.UseCase;
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
import java.util.Random;
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
        entity.setPassword(generateRandomPassword());
        User userEntity = userRepository.save(entity);
        setEmailConfigurationForSignup(userEntity);
        return Optional.of(new CreateUserResponse("200","Successfully","User has been successfully created"));
    }

    private void validateRequest(CreateUserRequest request) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9-.]*@gmail[.]com");
        Matcher emailMatcher = emailPattern.matcher(request.email());
        Boolean isValidEmail = emailMatcher.matches();
        Optional<User> userEntity = this.userRepository.findByEmail(request.email());
        if (isValidEmail.equals(false)) {
            throw new CustomResponseException( "0", "failed", "Email must be start with one or more word characters, periods, plus signs, or dashes, followed by an \"@\" symbol, and ending with the specific characters \"gmail.com\".");

        }
        if (userEntity.isPresent()) {
            throw new CustomResponseException("0", "failed", "Email address already taken!");
        }
    }


    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@#";
        int passwordLength = 8;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private void setEmailConfigurationForSignup(User userEntity) throws IOException, URISyntaxException, InterruptedException {
        String subject = "Login Details";
        String htmlContent = """
                Thank you for signing up in easy money request.Here is your login details:
                 """ +
                " Your email " + userEntity.getEmail()
                +
                " Your password " + userEntity.getPassword();

        EmailHandler sendEmail = new EmailHandler(userEntity.getEmail(), subject, htmlContent);
        generateEmailRequest(sendEmail);

    }

    private void generateEmailRequest(EmailHandler emailHandler) throws IOException, URISyntaxException, InterruptedException {
        String json = new ObjectMapper().writeValueAsString(emailHandler);
        HttpRequest httpRequest = HttpRequest.newBuilder()

                .uri(new URI("http://172.16.16.229:8081/email/verification"))
                .header("Content-Type", "application/json") // Set the content type header
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> stringHttpResponse =  httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(stringHttpResponse.body());
    }

}
