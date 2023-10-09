package citytech.global.usecase.login;

import citytech.global.platform.exception.CustomResponseException;
import citytech.global.platform.security.SecurityUtils;
import citytech.global.platform.usecase.UseCase;
import citytech.global.repository.User;
import citytech.global.repository.UserRepository;
import jakarta.inject.Inject;

import java.util.Optional;

public class LoginUseCase implements UseCase<LoginRequest,LoginResponse> {

    private UserRepository userRepository;

    @Inject
    public LoginUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<LoginResponse> execute(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent()){
            if(user.get().getPassword().equals(request.password())){
                String token = generateToken(user.get().getEmail(),user.get().getUserType().toString(),user.get().getUserId());
                return Optional.of(new LoginResponse("User Login Successful",token));
            }
            throw new CustomResponseException("0", "failed", "Invalid credential");
        }
        throw new CustomResponseException("0", "failed", "Invalid credential");    }

    private String generateToken(String email,String userType, long userId){
        SecurityUtils securityUtils = new SecurityUtils();
        return securityUtils.token(email,userType,userId);
    }
}

