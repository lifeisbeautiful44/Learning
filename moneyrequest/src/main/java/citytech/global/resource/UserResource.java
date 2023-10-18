package citytech.global.resource;


import citytech.global.converter.UserConverter;
import citytech.global.platform.exception.MoneyRequestException;
import citytech.global.platform.exception.MoneyRequestExceptionType;
import citytech.global.platform.restapiresponse.RestResponse;
import citytech.global.resource.payload.CreateUserPayload;
import citytech.global.resource.payload.LoginPayLoad;
import citytech.global.usecase.create.CreateUserRequest;
import citytech.global.usecase.create.CreateUserResponse;
import citytech.global.usecase.create.CreateUserUseCase;
import citytech.global.usecase.csv.DownloadToCsvUseCase;
import citytech.global.usecase.login.LoginRequest;
import citytech.global.usecase.login.LoginResponse;
import citytech.global.usecase.login.LoginUseCase;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Controller("/api/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final CreateUserUseCase createUserUseCase;
    private final LoginUseCase loginUseCase;

    private final DownloadToCsvUseCase downloadToCsvUseCase;

    @Inject
    public UserResource(CreateUserUseCase createUserUseCase, LoginUseCase loginUseCase, DownloadToCsvUseCase downloadToCsvUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.loginUseCase = loginUseCase;
        this.downloadToCsvUseCase = downloadToCsvUseCase;
    }


    @Post("/create/user")
    public HttpResponse<RestResponse<CreateUserResponse>> createUser(@Body CreateUserPayload payload) throws IOException, URISyntaxException, InterruptedException {

        CreateUserRequest request = UserConverter.toRequest(payload);

        Optional<CreateUserResponse> response = createUserUseCase.execute(request);
        if (response.isPresent()) {
            return HttpResponse.ok(RestResponse.success(response.get().data()));
        }

        throw new MoneyRequestException(MoneyRequestExceptionType.USER_NOT_FOUND);

    }

    @Post("/login")
    public HttpResponse<RestResponse<LoginResponse>> loginUser(@Body LoginPayLoad payLoad) {

        LoginRequest loginRequest = UserConverter.toLoginRequest(payLoad);
        Optional<LoginResponse> loginResponse = loginUseCase.execute(loginRequest);
        if (loginResponse.isPresent()) {
            return HttpResponse.ok(RestResponse.success(loginResponse.get()));
        } else {
            throw new MoneyRequestException(MoneyRequestExceptionType.INVALID_CREDENTIAL);
        }
    }

    @Get("/download")
    public void downloadFile() {
        downloadToCsvUseCase.execute();
    }


}
