package citytech.global.resource;


import citytech.global.platform.restresponse.Response;
import citytech.global.resource.payload.MapDetailsPayload;
import citytech.global.usecase.LocationUseCase;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.URISyntaxException;


@Controller("/api")
public class GoogleMapController {
    private final LocationUseCase locationUseCase;
    @Inject
    public GoogleMapController(LocationUseCase locationUseCase) {
        this.locationUseCase = locationUseCase;
    }
    @Post("checking")
    public Response googleMap(@Body MapDetailsPayload mapRequestBody) throws IOException, InterruptedException, URISyntaxException {
       Response response =   locationUseCase.execute(mapRequestBody);
       return response;
    }
}
