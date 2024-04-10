package at.ac.fhcampuswien.fhmdb.models;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Movie {
    private String title;
    private String description;
    private List<String> genres;
    private int releaseYear;
    private double rating;
    private String director;
    private List<String> mainCast;

    // Updated constructor to initialize all fields
    public Movie(String title, String description, List<String> genres, int releaseYear,
                 double rating, String director, List<String> mainCast) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.director = director;
        this.mainCast = mainCast;
    }

    // Getter and Setter methods for all fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public void setMainCast(List<String> mainCast) {
        this.mainCast = mainCast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return getReleaseYear() == movie.getReleaseYear() &&
                Double.compare(movie.getRating(), getRating()) == 0 &&
                Objects.equals(getTitle(), movie.getTitle()) &&
                Objects.equals(getDescription(), movie.getDescription()) &&
                Objects.equals(getGenres(), movie.getGenres()) &&
                Objects.equals(getDirector(), movie.getDirector()) &&
                Objects.equals(getMainCast(), movie.getMainCast());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDescription(), getGenres(), getReleaseYear(), getRating(), getDirector(), getMainCast());
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", releaseYear=" + releaseYear +
                ", rating=" + rating +
                ", director='" + director + '\'' +
                ", mainCast=" + mainCast +
                '}';
    }
}
