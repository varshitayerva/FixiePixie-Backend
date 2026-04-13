package com.example.reviews.controller;

import com.example.reviews.dto.reviewDTO;
import com.example.reviews.dto.reviewUpdateDTO;
import com.example.reviews.service.reviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class controller {
    @Autowired
    private reviewService reviewService;
    @PostMapping("/give")
    public ResponseEntity<reviewDTO> saveReview(@Valid @RequestBody reviewDTO reviewDTO) {
        reviewDTO review = reviewService.createReview(reviewDTO);
        return ResponseEntity.ok().body(review);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<reviewDTO> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody reviewUpdateDTO reviewDTO) {
        reviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<reviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }


}
