package citytech.global.platform.restclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Controller

public class RestClient {

    @Get("/conusme/restclient")
    public void callRestClient() throws IOException, InterruptedException, URISyntaxException {

        RequestBody requestBody = new RequestBody();
        requestBody.setEmail("satishsubedi18@gmail.com");
        requestBody.setPassword("hello");

          String json = new ObjectMapper().writeValueAsString(requestBody);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://172.16.16.163:8080/user/login"))
                .header("Content-Type", "application/json") // Set the content type header
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();

        java.net.http.HttpResponse<String> stringHttpResponse =  httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
        System.out.println(stringHttpResponse.body());
    }
}