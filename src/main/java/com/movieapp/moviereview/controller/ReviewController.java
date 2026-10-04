package com.movieapp.moviereview.controller;

import com.movieapp.moviereview.dto.ReviewRequest;
import com.movieapp.moviereview.dto.ReviewResponse;
import com.movieapp.moviereview.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/movies/{movieId}/reviews")
    public ResponseEntity<ReviewResponse> addReview(@PathVariable Long movieId,
                                                     @Valid @RequestBody ReviewRequest request,
                                                     Authentication authentication) {
        String email = authentication.getName();
        return new ResponseEntity<>(reviewService.addReview(movieId, request, email), HttpStatus.CREATED);
    }

    @GetMapping("/movies/{movieId}/reviews")
    public List<ReviewResponse> getReviews(@PathVariable Long movieId) {
        return reviewService.getReviewsForMovie(movieId);
    }

    @PutMapping("/reviews/{id}")
    public ReviewResponse updateReview(@PathVariable Long id,
                                       @Valid @RequestBody ReviewRequest request,
                                       Authentication authentication) {
        String email = authentication.getName();
        return reviewService.updateReview(id, request, email);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id,
                                               Authentication authentication) {
        String email = authentication.getName();
        reviewService.deleteReview(id, email);
        return ResponseEntity.ok("Review deleted successfully");
    }
}