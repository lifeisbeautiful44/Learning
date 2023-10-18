package citytech.global.usecase.login;

import citytech.global.converter.UserConverter;
import citytech.global.platform.exception.MoneyRequestException;
import citytech.global.platform.exception.MoneyRequestExceptionType;
import citytech.global.platform.security.SecurityUtils;
import citytech.global.platform.usecase.UseCase;
import citytech.global.repository.User;
import citytech.global.repository.UserRepository;
import jakarta.inject.Inject;

import java.util.Optional;

public class LoginUseCase implements UseCase<LoginRequest, LoginResponse> {

    private UserRepository userRepository;

    @Inject
    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<LoginResponse> execute(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isPresent()) {
            User userDetails = user.get();
            if (userDetails.getPassword().equals(request.password())) {
                String token = generateToken(userDetails.getEmail(), userDetails.getUserType().toString(), userDetails.getUserId());
                LoginResponse loginResponse = UserConverter.toResponse(token);
                return Optional.of(loginResponse);
            } else {
                throw new MoneyRequestException(MoneyRequestExceptionType.INVALID_CREDENTIAL);
            }
        } else {
            throw new MoneyRequestException(MoneyRequestExceptionType.USER_NOT_FOUND);
        }
    }

    private String generateToken(String email, String userType, long userId) {
        SecurityUtils securityUtils = new SecurityUtils();
        return securityUtils.token(email, userType, userId);
    }
}

