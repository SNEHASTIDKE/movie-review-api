package com.movieapp.moviereview.repository;

import com.movieapp.moviereview.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findByGenreIgnoreCase(String genre, Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}