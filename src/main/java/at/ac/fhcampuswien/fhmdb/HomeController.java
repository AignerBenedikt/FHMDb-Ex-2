package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.MovieApi;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.Map;

public class HomeController implements Initializable {
    private static List<Movie> movies;
    // FXML Fields
    @FXML
    private JFXButton searchBtn;
    @FXML
    private TextField searchField;
    @FXML
    private JFXListView<Movie> movieListView;
    @FXML
    private JFXComboBox<String> genreComboBox;
    @FXML
    private JFXComboBox<Integer> releaseYearComboBox;
    @FXML
    private JFXComboBox<Double> ratingComboBox;
    @FXML
    private JFXButton sortBtn;
    @FXML
    private void resetBtnClicked(ActionEvent event) {
        resetFiltersAndSort();
    }

    // Other Fields
    ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    SortedState sortedState = SortedState.NONE;
    private static MovieApi movieAPI = new MovieApi(); // Ensure you have this class created to interact with the API
    List<Movie> allMovies;

    // Initialization Method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupLayout();
        initializeState();

        // Listener hinzufügen, um die Suchfunktion sofort zu aktivieren
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFiltersAndSort();
        });
    }

    // Setup Methods
    private void setupLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell());

        List<String> genres = Arrays.stream(Genre.values())
                .map(Enum::toString)
                .collect(Collectors.toList());

        genreComboBox.getItems().add("Select Genre");
        genreComboBox.getItems().addAll(genres);
        genreComboBox.setPromptText("Filter by Genre");

        releaseYearComboBox.setPromptText("Filter by Release Year");
        Integer[] releaseYears = new Integer[78];
        for (int i = 0; i < 78; i++) {
            releaseYears[i] = 2023 - i;
        }
        releaseYearComboBox.getItems().addAll(releaseYears);

        ratingComboBox.setPromptText("Filter by Rating");
        Double[] ratings = new Double[]{1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00};
        ratingComboBox.getItems().addAll(ratings);
    }

    // State Initialization Method
    public void initializeState() {
        movieAPI = new MovieApi();
        try {
            allMovies = movieAPI.getAllMovies();
            observableMovies.addAll(allMovies);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sortedState = SortedState.NONE;
    }

    private void resetFiltersAndSort() {
        searchField.clear();
        genreComboBox.getSelectionModel().clearSelection();
        genreComboBox.setPromptText("Filter by Genre");

        releaseYearComboBox.getSelectionModel().clearSelection();
        releaseYearComboBox.setPromptText("Filter by Release Year");

        ratingComboBox.getSelectionModel().clearSelection();
        ratingComboBox.setPromptText("Filter by Rating");

        loadMoviesFromAPI();
        sortedState = SortedState.NONE;
        movieListView.setItems(observableMovies);
    }

    // Action Methods (FXML event handlers)
    @FXML
    private void searchBtnClicked(ActionEvent event) {
        applyFiltersAndSort();
    }

    @FXML
    private void sortBtnClicked(ActionEvent event) {
        sortMovies(sortedState);
    }

    @FXML
    private void genreComboBoxChanged(ActionEvent event) {
        applyFiltersAndSort();
    }

    // Business Logic Methods
    private void applyFiltersAndSort() {
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();

        applyAllFilters(searchQuery, genre);
        sortMovies(sortedState);
    }

    void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("Select Genre")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }

    List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;

        return movies.stream()
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;

        return movies.stream()
                .filter(movie -> movie.getGenres().contains(genre))
                .collect(Collectors.toList());
    }

    private void sortMovies(SortedState ascending) {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.ASCENDING;
        } else {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.DESCENDING;
        }
    }
    public String getMostPopularActor(List<Movie> movies) {
        // Zähle die Häufigkeit jedes Schauspielers im mainCast
        Map<String, Long> actorCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        // Ermittle den Schauspieler mit der höchsten Häufigkeit
        return actorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // Rückgabe des Schauspielers oder null, falls Liste leer
    }

    // Funktion: int getLongestMovieTitle(List<Movie> movies)
    public int getLongestMovieTitle(List<Movie> movies) {
        // Filtere nach dem Film mit dem längsten Titel und erhalte die Länge des Titels
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0); // Rückgabe von 0, falls Liste leer
    }

    // Funktion: long countMoviesFrom(List<Movie> movies, String director)
    public long countMoviesFrom(List<Movie> movies, String director) {
        // Zähle die Filme, bei denen der Regisseur mit dem übergebenen Namen übereinstimmt
        return movies.stream()
                .filter(movie -> movie.getDirector().equals(director))
                .count();
    }

    // Funktion: List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear)
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        // Filtere die Filme basierend auf dem Veröffentlichungsjahr
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    public void sortMovies() {
    }

    private void loadMoviesFromAPI() {
        try {
            List<Movie> movies = movieAPI.getAllMovies();
            observableMovies.setAll(movies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

