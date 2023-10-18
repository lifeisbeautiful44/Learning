package citytech.global.converter;

import citytech.global.platform.constraints.UserType;
import citytech.global.platform.restapiresponse.ResponseType;
import citytech.global.repository.User;
import citytech.global.resource.payload.CreateUserPayload;
import citytech.global.resource.payload.LoginPayLoad;
import citytech.global.usecase.create.CreateUserRequest;
import citytech.global.usecase.create.CreateUserResponse;
import citytech.global.usecase.login.LoginRequest;
import citytech.global.usecase.login.LoginResponse;


public class UserConverter {
    public static CreateUserRequest toRequest(CreateUserPayload payload) {

        return new CreateUserRequest(
                payload.firstName(),
                payload.lastName(),
                payload.email(),
                payload.userType()
        );
    }

    public static User toEntity(CreateUserRequest request) {
        User userEntity = new User();
        userEntity.setFirstName(request.firstName());
        userEntity.setLastName(request.lastName());
        userEntity.setEmail(request.email());
        userEntity.setUserType(UserType.valueOf(request.userType()));
        return userEntity;
    }

    public static LoginRequest toLoginRequest(LoginPayLoad payLoad) {
        return new LoginRequest(
                payLoad.email(),
                payLoad.password()
        );
    }

    public static CreateUserResponse toResponse() {
        return new CreateUserResponse(ResponseType.USER_CREATED_SUCCESSFULLY.getData());
    }

    public static LoginResponse toResponse(String token) {
        return new LoginResponse(token);
    }

}
