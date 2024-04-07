package at.ac.fhcampuswien.fhmdb;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class API {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    // Method to get all movies without any filters
    public String getAllMovies() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "movies"))
                .header("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Failed to get movies: " + response.statusCode());
        }
    }

    // Method to search movies with filters such as free text and genre
    public String searchMovies(String userinput, String genre) throws Exception {
        String encodedUserInput = URLEncoder.encode(userinput, StandardCharsets.UTF_8);
        String encodedGenre = URLEncoder.encode(genre, StandardCharsets.UTF_8);
        String requestURL = BASE_URL + "movies?query=" + encodedUserInput + "&genre=" + encodedGenre;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestURL))
                .header("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Failed to search movies: " + response.statusCode());
        }
    }
}