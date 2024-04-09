package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Movie {
    private final String title;
    private final String description;
    private final List<Genre> genres;
    private final int releaseYear;
    private final double rating;
    private String director;
    private List<String> mainCast;

    public Movie(String title, String description, List<Genre> genres, int releaseYear, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Movie other)) {
            return false;
        }
        return this.title.equals(other.title) && this.description.equals(other.description) && this.genres.equals(other.genres) && this.releaseYear == other.releaseYear && Double.compare(this.rating, other.rating) == 0;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public double getRating() {
        return rating;
    }



    public List<String> getMainCast() {
        return mainCast;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDirector() {
        return director;
    }


    public static List<Movie> initializeMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(
                "Life Is Beautiful",
                "When an open-minded Jewish librarian and his son become victims of the Holocaust, he uses a perfect mixture of will, humor, and imagination to protect his son from the dangers around their camp.",
                Arrays.asList(Genre.DRAMA, Genre.ROMANCE),
                1997,
                8.6));
        movies.add(new Movie(
                "The Usual Suspects",
                "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                Arrays.asList(Genre.CRIME, Genre.DRAMA, Genre.MYSTERY),
                1995,
                8.5));
        movies.add(new Movie(
                "Puss in Boots",
                "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                Arrays.asList(Genre.COMEDY, Genre.FAMILY, Genre.ANIMATION),
                2011,
                6.6));
        movies.add(new Movie(
                "Avatar",
                "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.",
                Arrays.asList(Genre.ANIMATION, Genre.DRAMA, Genre.ACTION),
                2009,
                7.8));
        movies.add(new Movie(
                "The Wolf of Wall Street",
                "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                Arrays.asList(Genre.DRAMA, Genre.ROMANCE, Genre.BIOGRAPHY),
                2013,
                8.2));
        return movies;
    }

        public String getMostPopularActor(List<Movie> movies) {
            if (movies == null || movies.isEmpty()) {
                return null;
            }

            return movies.stream()
                    .flatMap(movie -> movie.getMainCast().stream())
                    .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

        // Method to get the length of the longest movie title from the given movies
        public int getLongestMovieTitle(List<Movie> movies) {
            if (movies == null || movies.isEmpty()) {
                return 0;
            }

            return movies.stream()
                    .mapToInt(movie -> movie.getTitle().length())
                    .max()
                    .orElse(0);
        }

        // Method to count movies from a specific director
        public long countMoviesFrom(List<Movie> movies, String director) {
            if (movies == null || movies.isEmpty() || director == null) {
                return 0;
            }

            return movies.stream()
                    .filter(movie -> director.equals(movie.getDirector()))
                    .count();
        }

        // Method to get movies released between two years
        public List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
            if (movies == null || movies.isEmpty()) {
                return List.of();
            }

            return movies.stream()
                    .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                    .collect(Collectors.toList());
        }
    }
