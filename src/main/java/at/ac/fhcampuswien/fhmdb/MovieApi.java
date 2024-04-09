package at.ac.fhcampuswien.fhmdb;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

public class MovieApi {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // Methode zum Abrufen aller Filme ohne Filter
    public String getAllMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .header("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to get movies: " + response);
            }
            return response.body().string();
        }
    }

    // Methode zum Suchen von Filmen mit Filterkriterien wie Freitext und Genre
    public String searchMovies(String userInput, String genre) throws IOException {
        String encodedUserInput = URLEncoder.encode(userInput, StandardCharsets.UTF_8);
        String encodedGenre = URLEncoder.encode(genre, StandardCharsets.UTF_8);
        String url = BASE_URL + "?query=" + encodedUserInput + "&genre=" + encodedGenre;

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to search movies: " + response);
            }
            return response.body().string();
        }
    }

    // Weitere Methoden für zusätzliche Endpunkte und Funktionen können hier hinzugefügt werden
}
