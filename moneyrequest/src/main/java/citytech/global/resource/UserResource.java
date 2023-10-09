package citytech.global.resource;


import citytech.global.converter.UserConverter;
import citytech.global.platform.exception.CustomResponseException;
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
public class UserResource  {

    private CreateUserUseCase createUserUseCase;
    private LoginUseCase loginUseCase;

    private DownloadToCsvUseCase downloadToCsvUseCase;

    @Inject
    public UserResource(CreateUserUseCase createUserUseCase, LoginUseCase loginUseCase, DownloadToCsvUseCase downloadToCsvUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.loginUseCase = loginUseCase;
        this.downloadToCsvUseCase = downloadToCsvUseCase;
    }



    @Post("/create/user")
    public HttpResponse<RestResponse> createUser(@Body CreateUserPayload payload) throws IOException, URISyntaxException, InterruptedException {

        CreateUserRequest request = UserConverter.toRequest(payload);
        try {
            Optional<CreateUserResponse> createUserResponse = createUserUseCase.execute(request);
                return HttpResponse.ok(RestResponse.success(createUserResponse.get()));
        } catch (CustomResponseException e) {
            return HttpResponse.badRequest(RestResponse.error(e.getCode(), e.getData()));
        }

    }
    @Post("/login")
    public HttpResponse<RestResponse> loginUser(@Body LoginPayLoad payLoad) {
        try {
            LoginRequest loginRequest = UserConverter.toLoginRequest(payLoad);
            Optional<LoginResponse> loginResponse = loginUseCase.execute(loginRequest);
                return HttpResponse.ok(RestResponse.success(loginResponse.get()));
        } catch (CustomResponseException e) {
            return HttpResponse.badRequest(RestResponse.error(e.getCode(), e.getData()));
        }
    }

    @Get("/download")
    public void downloadFile()
    {
         downloadToCsvUseCase.execute();
    }


}
