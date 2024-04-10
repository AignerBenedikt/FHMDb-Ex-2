package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
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

public class HomeController implements Initializable {
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

    // Other Fields
    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private SortedState sortedState = SortedState.NONE;
    private MovieAPI movieAPI = new MovieAPI(); // Ensure you have this class created to interact with the API
    private List<Movie> allMovies;

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
        movieAPI = new MovieAPI();
        try {
            allMovies = movieAPI.getAllMovies();
            observableMovies.addAll(allMovies);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sortedState = SortedState.NONE;
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

    private void applyAllFilters(String searchQuery, Object genre) {
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

    private List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;

        return movies.stream()
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .collect(Collectors.toList());
    }

    private List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
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
}
