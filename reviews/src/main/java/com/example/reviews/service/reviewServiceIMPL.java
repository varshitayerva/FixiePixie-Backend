package com.example.reviews.service;



import com.example.reviews.dto.reviewDTO;

import com.example.reviews.dto.reviewUpdateDTO;

import com.example.reviews.entity.review;

import com.example.reviews.repository.reviewRepository;

import com.example.reviews.utility.ReviewNotFound;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



import java.util.List;

import java.util.stream.Collectors;



@Service

public class reviewServiceIMPL implements reviewService {

    @Autowired

    private reviewRepository reviewRepository;

    @Override
    public reviewDTO createReview(reviewDTO reviewDTO) {
        // 1. Save all details to Database
        review reviewEntity = review.builder()
                .userId(reviewDTO.getUserId())
                .userName(reviewDTO.getUserName())
                .comment(reviewDTO.getComment())
                .bookingId(reviewDTO.getBookingId())
                .providerServiceId(reviewDTO.getProviderServiceId())
                .rating(reviewDTO.getRating())
                .build();

        review savedReview = reviewRepository.save(reviewEntity);

        // 2. Return ONLY name, rating, and comment
        return reviewDTO.builder()
                .userName(savedReview.getUserName())
                .rating(savedReview.getRating())
                .comment(savedReview.getComment())
                .build();
    }

    @Override
    public List<reviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::mapToMinimalDTO)
                .collect(Collectors.toList());
    }

    private reviewDTO mapToMinimalDTO(review entity) {
        // This ensures the userName from DB is put into the list
        return reviewDTO.builder()
                .userName(entity.getUserName())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .build();
    }

    @Override
    public reviewDTO updateReview(Long id, reviewUpdateDTO updateDTO) {
        review existing = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFound("Review not found"));
        existing.setRating(updateDTO.getRating());
        existing.setComment(updateDTO.getComment());
        review updated = reviewRepository.save(existing);
        return mapToMinimalDTO(updated);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }


}



