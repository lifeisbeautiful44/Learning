package citytech.global.usecase;

import citytech.global.platform.restresponse.ApiResponse;
import citytech.global.platform.restresponse.Response;
import citytech.global.resource.payload.MapDetailsPayload;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URISyntaxException;

@Singleton
public class LocationUseCase  {

   private final GeoCodingUseCase geoCodingService;



   public LocationUseCase(GeoCodingUseCase geoCodingService) {
      this.geoCodingService = geoCodingService;
   }

   public Response execute(MapDetailsPayload mapDetailsPayload) throws IOException, InterruptedException, URISyntaxException {
      ApiResponse apiResponse = geoCodingService.getGeoCodingDetails(mapDetailsPayload);

      Response response = new Response();
      response.setCode(200);
      response.setMessage("success");
      response.setData(apiResponse);
      return response;
   }

}
