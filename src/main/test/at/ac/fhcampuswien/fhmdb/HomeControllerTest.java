package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {
    private static HomeController homeController;
    @BeforeAll
    static void init() {
        homeController = new HomeController();
    }

    @Test
    void at_initialization_allMovies_and_observableMovies_should_be_filled_and_equal() {
        homeController.initializeState();
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }

    @Test
    void if_not_yet_sorted_sort_is_applied_in_ascending_order() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.NONE;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
        );

        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void if_last_sort_ascending_next_sort_should_be_descending() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.ASCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(
        );

        assertEquals(expected, homeController.observableMovies);
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() {
        // given
        homeController.initializeState();
        homeController.sortedState = SortedState.DESCENDING;

        // when
        homeController.sortMovies();

        // then
        List<Movie> expected = Arrays.asList(

        );

        assertEquals(expected, homeController.observableMovies);

    }

    @Test
    void query_filter_matches_with_lower_and_uppercase_letters(){
        // given
        homeController.initializeState();
        String query = "IfE";

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        List<Movie> expected = Arrays.asList(
        );

        assertEquals(expected, actual);
    }

    @Test
    void query_filter_with_null_movie_list_throws_exception(){
        // given
        homeController.initializeState();
        String query = "IfE";

        // when and then
        assertThrows(IllegalArgumentException.class, () -> homeController.filterByQuery(null, query));
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.initializeState();
        String query = null;

        // when
        List<Movie> actual = homeController.filterByQuery(homeController.observableMovies, query);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_with_null_value_returns_unfiltered_list() {
        // given
        homeController.initializeState();
        Genre genre = null;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(homeController.observableMovies, actual);
    }

    @Test
    void genre_filter_returns_all_movies_containing_given_genre() {
        // given
        homeController.initializeState();
        Genre genre = Genre.DRAMA;

        // when
        List<Movie> actual = homeController.filterByGenre(homeController.observableMovies, genre);

        // then
        assertEquals(4, actual.size());
    }

    @Test
    void no_filtering_ui_if_empty_query_or_no_genre_is_set() {
        // given
        homeController.initializeState();

        // when
        homeController.applyAllFilters("", null);

        // then
        assertEquals(homeController.allMovies, homeController.observableMovies);
    }
    @Test
    public void testGetMostPopularActor() {
        List<Movie> movies = Arrays.asList(
                new Movie("The Shawshank Redemption", "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                        Arrays.asList("Drama"), 1994, 9.3, "Frank Darabont", Arrays.asList("Tim Robbins", "Morgan Freeman")),
                new Movie("The Godfather", "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
                        Arrays.asList("Crime", "Drama"), 1972, 9.2, "Francis Ford Coppola", Arrays.asList("Marlon Brando", "Al Pacino", "James Caan")),
                new Movie("The Godfather: Part II", "The early life and career of Vito Corleone in 1920s New York City is portrayed, while his son, Michael, expands and tightens his grip on the family crime syndicate.",
                        Arrays.asList("Crime", "Drama"), 1974, 9.0, "Francis Ford Coppola", Arrays.asList("Al Pacino", "Robert De Niro", "Robert Duvall"))
        );
        HomeController controller = new HomeController();
        assertEquals("Al Pacino", controller.getMostPopularActor(movies));
    }

    @Test
    public void testGetLongestMovieTitle() {
        List<Movie> movies = Arrays.asList(
                new Movie("The Dark Knight", "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
                        Arrays.asList("Action", "Crime", "Drama"), 2008, 9.0, "Christopher Nolan", Arrays.asList("Christian Bale", "Heath Ledger", "Aaron Eckhart")),
                new Movie("The Lord of the Rings: The Return of the King", "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
                        Arrays.asList("Action", "Adventure", "Drama"), 2003, 8.9, "Peter Jackson", Arrays.asList("Elijah Wood", "Viggo Mortensen", "Ian McKellen"))
        );
        HomeController controller = new HomeController();
        assertEquals(45, controller.getLongestMovieTitle(movies));
    }

    @Test
    public void testCountMoviesFrom() {
        List<Movie> movies = Arrays.asList(
                new Movie("The Godfather", "An organized crime dynasty's aging patriarch transfers control of his clandestine empire to his reluctant son.",
                        Arrays.asList("Crime", "Drama"), 1972, 9.2, "Francis Ford Coppola", Arrays.asList("Marlon Brando", "Al Pacino", "James Caan")),
                new Movie("The Godfather: Part II", "The early life and career of Vito Corleone in 1920s New York City is portrayed, while his son, Michael, expands and tightens his grip on the family crime syndicate.",
                        Arrays.asList("Crime", "Drama"), 1974, 9.0, "Francis Ford Coppola", Arrays.asList("Al Pacino", "Robert De Niro", "Robert Duvall")),
                new Movie("The Lord of the Rings: The Return of the King", "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
                        Arrays.asList("Action", "Adventure", "Drama"), 2003, 8.9, "Peter Jackson", Arrays.asList("Elijah Wood", "Viggo Mortensen", "Ian McKellen"))
        );
        HomeController controller = new HomeController();
        assertEquals(2, controller.countMoviesFrom(movies, "Francis Ford Coppola"));
    }

    @Test
    public void testGetMoviesBetweenYears() {
        List<Movie> movies = Arrays.asList(
                new Movie("The Lord of the Rings: The Return of the King", "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
                        Arrays.asList("Action", "Adventure", "Drama"), 2003, 8.9, "Peter Jackson", Arrays.asList("Elijah Wood", "Viggo Mortensen", "Ian McKellen")),
                new Movie("Gladiator", "A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.",
                        Arrays.asList("Action", "Adventure", "Drama"), 2000, 8.5, "Ridley Scott", Arrays.asList("Russell Crowe", "Joaquin Phoenix", "Connie Nielsen")),
                new Movie("The Godfather: Part II", "The early life and career of Vito Corleone in 1920s New York City is portrayed, while his son, Michael, expands and tightens his grip on the family crime syndicate.",
                        Arrays.asList("Crime", "Drama"), 1974, 9.0, "Francis Ford Coppola", Arrays.asList("Al Pacino", "Robert De Niro", "Robert Duvall"))
        );
        HomeController controller = new HomeController();
        List<Movie> filteredMovies = controller.getMoviesBetweenYears(movies, 2000, 2010);
        assertEquals(2, filteredMovies.size());
    }
}
