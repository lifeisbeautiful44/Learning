package citytech.global.usecase;

import citytech.global.platform.restresponse.ApiResponse;
import citytech.global.resource.payload.MapDetailsPayload;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeoCodingUseCase {

    public ApiResponse getGeoCodingDetails(MapDetailsPayload mapDetailsPayload) throws IOException, InterruptedException, URISyntaxException {
        String jsonResponse = getApiResponseFromExternalService(mapDetailsPayload);
        ApiResponse apiResponse = mapJsonResponseToApiResponse(jsonResponse);
        return apiResponse;
    }

    private String getApiResponseFromExternalService(MapDetailsPayload mapDetailsPayload) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("https://api.geoapify.com/v1/geocode/reverse?lat=" + mapDetailsPayload.getLatitude() + "&lon=" + mapDetailsPayload.getLongitude() + "&apiKey=564884ec8cc1419286c6bd308ea44bf8");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> stringHttpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return stringHttpResponse.body();
    }

    private ApiResponse mapJsonResponseToApiResponse(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        JsonNode propertiesNode = jsonNode.path("features").get(0).path("properties");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCountry(propertiesNode.path("country").asText());
        apiResponse.setCountryCode(propertiesNode.path("country_code").asText());
        apiResponse.setState(propertiesNode.path("state").asText());
        apiResponse.setCity(propertiesNode.path("city").asText());
        apiResponse.setPostcode(propertiesNode.path("postcode").asText());
        apiResponse.setStreet(propertiesNode.path("street").asText());
        return apiResponse;
    }
}
