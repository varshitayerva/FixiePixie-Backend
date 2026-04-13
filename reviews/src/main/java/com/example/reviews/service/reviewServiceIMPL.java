package com.example.reviews.service;



import com.example.reviews.client.ProviderClient;
import com.example.reviews.client.UserClient;

import com.example.reviews.dto.UserResponseDTO;
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

    @Autowired
    ProviderClient providerClient;

    @Autowired
    UserClient userClient;

    @Override
    public reviewDTO createReview(reviewDTO reviewDTO) {

        UserResponseDTO response = userClient.getUserById(reviewDTO.getUserId());


        String userName = (response != null && response.getData() != null)
                ? response.getData().getName()
                : "Unknown User";


        String serviceName = providerClient.getServiceById(reviewDTO.getProviderServiceId());


        if (userName != null && serviceName != null) {
            review reviewEntity = review.builder()
                    .userId(reviewDTO.getUserId())
                    .providerServiceId(reviewDTO.getProviderServiceId())
                    .userName(userName)

                    .rating(reviewDTO.getRating())
                    .comment(reviewDTO.getComment())
                    .build();

            review saved = reviewRepository.save(reviewEntity);


            return reviewDTO.builder()
                    .id(saved.getId())
                    .userId(saved.getUserId())
                    .userName(userName)
                    .providerServiceId(saved.getProviderServiceId())
                    .rating(saved.getRating())
                    .comment(saved.getComment())
                    .createdAt(saved.getCreatedAt())
                    .build();
        }

        throw new RuntimeException("Validation Failed: User or Service does not exist.");
    }



    @Override
    public List<reviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::mapToMinimalDTO)
                .collect(Collectors.toList());
    }

    private reviewDTO mapToMinimalDTO(review entity) {

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
        return mapToDTOWithDetails(updated);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }


    private reviewDTO mapToDTOWithDetails(review entity) {

        UserResponseDTO response = userClient.getUserById(entity.getUserId());


        String userName = (response != null && response.getData() != null)
                ? response.getData().getName()
                : "Unknown User";

        return reviewDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userName(userName) // Fetched from User Service
                .providerServiceId(entity.getProviderServiceId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}



