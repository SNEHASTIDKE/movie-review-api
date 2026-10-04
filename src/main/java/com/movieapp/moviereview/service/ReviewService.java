package com.movieapp.moviereview.service;

import com.movieapp.moviereview.dto.ReviewRequest;
import com.movieapp.moviereview.dto.ReviewResponse;
import com.movieapp.moviereview.entity.Movie;
import com.movieapp.moviereview.entity.Review;
import com.movieapp.moviereview.entity.User;
import com.movieapp.moviereview.exception.BadRequestException;
import com.movieapp.moviereview.exception.ForbiddenException;
import com.movieapp.moviereview.exception.ResourceNotFoundException;
import com.movieapp.moviereview.repository.MovieRepository;
import com.movieapp.moviereview.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository,
                         MovieRepository movieRepository,
                         MovieService movieService,
                         UserService userService) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.userService = userService;
    }

    @Transactional
    public ReviewResponse addReview(Long movieId, ReviewRequest request, String userEmail) {
        Movie movie = movieService.getMovieById(movieId);
        User user = userService.getByEmail(userEmail);

        if (reviewRepository.existsByMovieIdAndUserId(movieId, user.getId())) {
            throw new BadRequestException("You have already reviewed this movie");
        }

        Review review = new Review();
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        updateAverageRating(movie);
        return toResponse(saved);
    }

    public List<ReviewResponse> getReviewsForMovie(Long movieId) {
        movieService.getMovieById(movieId);
        return reviewRepository.findByMovieId(movieId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request, String userEmail) {
        Review review = findReview(reviewId);
        User user = userService.getByEmail(userEmail);
        checkOwner(review, user.getId());

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        updateAverageRating(review.getMovie());
        return toResponse(saved);
    }

    @Transactional
    public void deleteReview(Long reviewId, String userEmail) {
        Review review = findReview(reviewId);
        User user = userService.getByEmail(userEmail);
        checkOwner(review, user.getId());

        Movie movie = review.getMovie();
        reviewRepository.delete(review);
        reviewRepository.flush();
        updateAverageRating(movie);
    }

    private Review findReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));
    }

    private void checkOwner(Review review, Long userId) {
        if (!review.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can modify only your own review");
        }
    }

    private void updateAverageRating(Movie movie) {
        Double avg = reviewRepository.findAverageRatingByMovieId(movie.getId());
        double rounded = (avg == null) ? 0.0 : Math.round(avg * 10.0) / 10.0;
        movie.setAverageRating(rounded);
        movieRepository.save(movie);
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getMovie().getId(),
                review.getMovie().getTitle(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}