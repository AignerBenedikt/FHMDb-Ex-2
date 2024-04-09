package at.ac.fhcampuswien.fhmdb;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

public class MovieApi {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // Method to get all movies without any filters
    public String getAllMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "movies")
                .header("User-Agent", "Java OkHttp3 Client")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Failed to get movies: " + response);
            return response.body().string();
        }
    }

    // Method to search movies with filters such as free text and genre
    public String searchMovies(String userinput, String genre) throws IOException {
        String encodedUserInput = URLEncoder.encode(userinput, StandardCharsets.UTF_8);
        String encodedGenre = URLEncoder.encode(genre, StandardCharsets.UTF_8);
        String requestURL = BASE_URL + "movies?query=" + encodedUserInput + "&genre=" + encodedGenre;

        Request request = new Request.Builder()
                .url(requestURL)
                .header("User-Agent", "Java OkHttp3 Client")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Failed to search movies: " + response);
            return response.body().string();
        }
    }
}
