package com.example.reviews.service;

import com.example.reviews.dto.reviewDTO;
import com.example.reviews.dto.reviewUpdateDTO;
import com.example.reviews.entity.review;
import com.example.reviews.repository.reviewRepository;
import com.example.reviews.utility.ReviewNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class reviewServiceIMPL implements reviewService {
    @Autowired
    private reviewRepository reviewRepository;


    @Override
    public reviewDTO createReview(reviewDTO reviewDTO) {
        review review = new review();
        // ID and CreatedAt are handled by DB/Hibernate
        review.setComment(reviewDTO.getComment());
        review.setBookingId(reviewDTO.getBookingId());
        review.setProviderServiceId(reviewDTO.getProviderServiceId());
        review.setRating(reviewDTO.getRating());

        review savedReview = reviewRepository.save(review);
        return mapToDTO(savedReview);
    }

    @Override
    public reviewDTO updateReview(Long id, reviewUpdateDTO reviewDTO) {
        review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFound("review not found"));

        existing.setRating(reviewDTO.getRating());
        existing.setComment(reviewDTO.getComment());

        return mapToDTO(reviewRepository.save(existing));
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // Helper method to keep your code DRY (Don't Repeat Yourself)
    private reviewDTO mapToDTO(review entity) {
        reviewDTO dto = new reviewDTO();
        dto.setId(entity.getId());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setBookingId(entity.getBookingId());
        dto.setProviderServiceId(entity.getProviderServiceId());
        dto.setRating(entity.getRating());
        return dto;
    }

}


