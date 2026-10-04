package com.movieapp.moviereview.controller;

import com.movieapp.moviereview.entity.Movie;
import com.movieapp.moviereview.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<Movie> getMovies(@RequestParam(required = false) String title,
                                 @RequestParam(required = false) String genre,
                                 Pageable pageable) {
        if (title != null) {
            return movieService.searchByTitle(title, pageable);
        }
        if (genre != null) {
            return movieService.getByGenre(genre, pageable);
        }
        return movieService.getAllMovies(pageable);
    }

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie deleted successfully");
    }
}