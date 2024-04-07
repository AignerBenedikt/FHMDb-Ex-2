package at.ac.fhcampuswien.fhmdb;


import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class API {
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/swagger-ui/index.html";

    public String getAllMovies() throws Exception {
        URL url = new URL(BASE_URL + "movies");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "http.agent");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString(); // The raw JSON response
        } finally {
            con.disconnect();
        }
    }
}