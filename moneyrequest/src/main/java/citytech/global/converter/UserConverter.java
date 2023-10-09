package citytech.global.converter;

import citytech.global.platform.constraints.UserType;
import citytech.global.repository.User;
import citytech.global.resource.payload.CreateUserPayload;
import citytech.global.resource.payload.LoginPayLoad;
import citytech.global.usecase.create.CreateUserRequest;
import citytech.global.usecase.create.CreateUserRequestBuilder;
import citytech.global.usecase.login.LoginRequest;
import citytech.global.usecase.login.LoginRequestBuilder;


public class UserConverter {
    public static CreateUserRequest toRequest(CreateUserPayload payload){
        return CreateUserRequestBuilder.builder()
                .firstName(payload.firstName())
                .lastName(payload.lastName())
                .email(payload.email())
                //    .password((payload.password()))
                .userType(payload.userType())
                .build();
    }
    public static User toEntity(CreateUserRequest request){
        User userEntity = new User();
        userEntity.setFirstName(request.firstName());
        userEntity.setLastName(request.lastName());
        userEntity.setEmail(request.email());
        //  userEntity.setPassword(request.password());
        userEntity.setUserType(UserType.valueOf(request.userType()));
        return userEntity;
    }
    public static LoginRequest toLoginRequest(LoginPayLoad payLoad){
        return LoginRequestBuilder.builder()
                .email(payLoad.email())
                .password(payLoad.password())
                .build();
    }

}
