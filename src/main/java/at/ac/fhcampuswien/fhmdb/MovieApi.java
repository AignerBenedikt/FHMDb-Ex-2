package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MovieApi {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final Gson GSON = new Gson();

    private static HttpUrl buildUrl(String query, Genre genre, String releaseYear, String ratingFrom) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        if (query != null) urlBuilder.addQueryParameter("query", query);
        if (genre != null) urlBuilder.addQueryParameter("genre", genre.toString());
        if (releaseYear != null) urlBuilder.addQueryParameter("releaseYear", releaseYear);
        if (ratingFrom != null) urlBuilder.addQueryParameter("ratingFrom", ratingFrom);

        return urlBuilder.build();
    }

    public static List<Movie> getMovies(String query, Genre genre, String releaseYear, String ratingFrom) {
        HttpUrl url = buildUrl(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent") // Wichtig für die API
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Anfrage fehlgeschlagen: " + response);
                return Collections.emptyList();
            }
            // Sicherstellen, dass die Antwort nicht null ist, bevor sie geparst wird
            String responseBody = response.body() != null ? response.body().string() : null;
            if (responseBody != null && !responseBody.isEmpty()) {
                return List.of(GSON.fromJson(responseBody, Movie[].class));
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Abrufen der Filme: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public static List<Movie> getAllMovies() {
        return getMovies(null, null, null, null);
    }
}
