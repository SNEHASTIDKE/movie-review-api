package com.movieapp.moviereview.service;

import com.movieapp.moviereview.entity.Movie;
import com.movieapp.moviereview.exception.ResourceNotFoundException;
import com.movieapp.moviereview.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    public Page<Movie> searchByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Movie> getByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenreIgnoreCase(genre, pageable);
    }

    public Movie updateMovie(Long id, Movie updated) {
        Movie existing = getMovieById(id);
        existing.setTitle(updated.getTitle());
        existing.setGenre(updated.getGenre());
        existing.setReleaseYear(updated.getReleaseYear());
        existing.setDirector(updated.getDirector());
        existing.setDescription(updated.getDescription());
        existing.setPosterUrl(updated.getPosterUrl());
        return movieRepository.save(existing);
    }

    public void deleteMovie(Long id) {
        Movie existing = getMovieById(id);
        movieRepository.delete(existing);
    }
}