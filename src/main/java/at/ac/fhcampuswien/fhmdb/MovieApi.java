package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MovieAPI {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";
    private final OkHttpClient client;
    private final Gson gson;

    public MovieAPI() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public List<Movie> getMovies(String query, String genre) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        boolean firstParam = true;

        if (query != null && !query.isEmpty()) {
            urlBuilder.append(firstParam ? "?" : "&").append("query=").append(query);
            firstParam = false;
        }

        if (genre != null && !genre.isEmpty()) {
            urlBuilder.append(firstParam ? "?" : "&").append("genre=").append(genre);
        }

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .addHeader("User-Agent", "http.agent") // Wichtig, um den 403 Statuscode zu vermeiden
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Type listType = new TypeToken<List<Movie>>() {}.getType();
            return gson.fromJson(response.body().string(), listType);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Beispiel Methode, um alle Filme ohne Filter zu laden
    public List<Movie> getAllMovies() throws IOException {
        return getMovies(null, null);
    }

    // Hier können weitere Methoden hinzugefügt werden, um die API mit anderen Endpunkten oder Parametern aufzurufen.
}
