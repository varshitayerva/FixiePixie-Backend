package com.example.reviews.service;

import com.example.reviews.dto.reviewDTO;
import com.example.reviews.dto.reviewUpdateDTO;

public interface reviewService {

        public reviewDTO createReview(reviewDTO review);
    public reviewDTO updateReview(Long id, reviewUpdateDTO reviewDTO);
    public void deleteReview(Long id);

}
