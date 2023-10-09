package citytech.global.usecase;

import citytech.global.platform.restresponse.ApiResponse;
import citytech.global.platform.restresponse.Response;
import citytech.global.resource.payload.MapDetailsPayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.MediaType;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class LocationUseCase {

   public Response execute(MapDetailsPayload mapDetailsPayload) throws IOException, InterruptedException, URISyntaxException {
      URI uri = new URI("https://api.geoapify.com/v1/geocode/reverse?lat=" + mapDetailsPayload.getLatitude() + "&lon=" + mapDetailsPayload.getLongitude() + "&apiKey=564884ec8cc1419286c6bd308ea44bf8");

      HttpRequest httpRequest = HttpRequest.newBuilder()
              .uri(uri)
              .header("Content-Type", MediaType.APPLICATION_JSON)
              .GET()
              .build();

      HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
      HttpResponse<String> stringHttpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
      String jsonResponse = stringHttpResponse.body();
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(jsonResponse);
      JsonNode propertiesNode = jsonNode
              .path("features")
              .get(0)
              .path("properties");

      ApiResponse apiresponse = new ApiResponse();
      apiresponse.setCountry(propertiesNode.path(("country")).asText());
      apiresponse.setCountryCode(propertiesNode.path(("country_code")).asText());
      apiresponse.setState(propertiesNode.path(("state")).asText());
      apiresponse.setCity(propertiesNode.path(("city")).asText());
      apiresponse.setPostcode(propertiesNode.path(("postcode")).asText());
      apiresponse.setStreet(propertiesNode.path(("street")).asText());

      Response response = new Response();
      response.setCode(200);
      response.setMessage("success");
      response.setData(apiresponse);
      return response;

   }


}
