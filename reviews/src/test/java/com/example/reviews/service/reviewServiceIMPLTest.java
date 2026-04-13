package com.example.reviews.service;

import com.example.reviews.client.ProviderClient;
import com.example.reviews.client.UserClient;
import com.example.reviews.dto.UserResponseDTO;
import com.example.reviews.dto.reviewDTO;
import com.example.reviews.dto.reviewUpdateDTO;
import com.example.reviews.entity.review;
import com.example.reviews.repository.reviewRepository;
import com.example.reviews.utility.ReviewNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class reviewServiceIMPLTest {

//    @Mock
//    private reviewRepository reviewRepository;
//
//    @Mock
//    private UserClient userClient;
//
//    @Mock
//    private ProviderClient providerClient;
//    @InjectMocks
//    private reviewServiceIMPL service;
//
//    @Test
//    void createReviewSavesAndReturnsMappedDto() {
//        reviewDTO request = reviewDTO.builder()
//                .providerServiceId(11L)
//                .bookingId(22L)
//                .rating(5)
//                .comment("Excellent work")
//                .build();
//
//        review saved = review.builder()
//                .id(1L)
//                .providerServiceId(11L)
//
//                .rating(5)
//                .comment("Excellent work")
//                .createdAt(LocalDateTime.of(2026, 4, 8, 10, 0))
//                .build();
//
//        when(reviewRepository.save(any(review.class))).thenReturn(saved);
//
//        reviewDTO result = service.createReview(request);
//
//        assertEquals(1L, result.getId());
//        assertEquals(11L, result.getProviderServiceId());
//        assertEquals(22L, result.getBookingId());
//        assertEquals(5, result.getRating());
//        assertEquals("Excellent work", result.getComment());
//        assertEquals(saved.getCreatedAt(), result.getCreatedAt());
//        verify(reviewRepository).save(any(review.class));
//    }
//
//    @Test
//    void updateReviewUpdatesExistingReview() {
//        review existing = review.builder()
//                .id(7L)
//                .providerServiceId(15L)
//                .bookingId(27L)
//                .rating(3)
//                .comment("Old comment")
//                .createdAt(LocalDateTime.of(2026, 4, 8, 11, 0))
//                .build();
//
//        review updated = review.builder()
//                .id(7L)
//                .providerServiceId(15L)
//                .rating(4)
//                .comment("Updated comment")
//                .createdAt(existing.getCreatedAt())
//                .build();
//
//        reviewUpdateDTO request = new reviewUpdateDTO();
//        request.setRating(4);
//        request.setComment("Updated comment");
//
//        when(reviewRepository.findById(7L)).thenReturn(Optional.of(existing));
//        when(reviewRepository.save(existing)).thenReturn(updated);
//
//        reviewDTO result = service.updateReview(7L, request);
//
//        assertEquals(7L, result.getId());
//        assertEquals(4, result.getRating());
//        assertEquals("Updated comment", result.getComment());
//        assertEquals(27L, result.getBookingId());
//        verify(reviewRepository).findById(7L);
//        verify(reviewRepository).save(existing);
//    }
//
//    @Test
//    void updateReviewThrowsWhenReviewDoesNotExist() {
//        reviewUpdateDTO request = new reviewUpdateDTO();
//        request.setRating(2);
//        request.setComment("Missing");
//
//        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());
//
//        ReviewNotFound exception = assertThrows(ReviewNotFound.class,
//                () -> service.updateReview(99L, request));
//
//        assertEquals("Review not found", exception.getMessage());
//        verify(reviewRepository).findById(99L);
//        verify(reviewRepository, never()).save(any(review.class));
//    }
//
//    @Test
//    void deleteReviewDelegatesToRepository() {
//        service.deleteReview(5L);
//
//        verify(reviewRepository).deleteById(5L);
//    }


    @Mock
    private reviewRepository reviewRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private ProviderClient providerClient;

    @InjectMocks
    private reviewServiceIMPL service;

    @Test
    void createReviewSavesAndReturnsMappedDto() {
        // 1. Setup Request DTO
        reviewDTO request = reviewDTO.builder()
                .userId(100L)
                .providerServiceId(11L)
                .rating(5)
                .comment("Excellent work")
                .build();

        // 2. Setup Entity that the DB would return
        review savedEntity = review.builder()
                .id(1L)
                .userId(100L)
                .providerServiceId(11L)
                .rating(5)
                .comment("Excellent work")
                .createdAt(LocalDateTime.of(2026, 4, 8, 10, 0))
                .build();

        // 3. Mock UserResponseDTO structure exactly as per your provided class
        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setSuccess(true);
        UserResponseDTO.UserData userData = new UserResponseDTO.UserData();
        userData.setName("Smruti");
        userResponse.setData(userData);

        // 4. Stubbing
        when(userClient.getUserById(100L)).thenReturn(userResponse);
        when(providerClient.getServiceById(11L)).thenReturn("Plumbing Service");
        when(reviewRepository.save(any(review.class))).thenReturn(savedEntity);

        // 5. Execution
        reviewDTO result = service.createReview(request);

        // 6. Assertions
        assertEquals(1L, result.getId());
        assertEquals("Smruti", result.getUserName());
        assertEquals(5, result.getRating());
        assertEquals("Excellent work", result.getComment());
        verify(reviewRepository).save(any(review.class));
    }

    @Test
    void updateReviewUpdatesExistingReview() {
        // 1. Setup existing record
        review existing = review.builder()
                .id(7L)
                .userId(100L)
                .providerServiceId(15L)
                .rating(3)
                .comment("Old comment")
                .createdAt(LocalDateTime.of(2026, 4, 8, 11, 0))
                .build();

        // 2. Setup updated record
        review updated = review.builder()
                .id(7L)
                .userId(100L)
                .providerServiceId(15L)
                .rating(4)
                .comment("Updated comment")
                .createdAt(existing.getCreatedAt())
                .build();

        reviewUpdateDTO request = new reviewUpdateDTO();
        request.setRating(4);
        request.setComment("Updated comment");

        // 3. Mock UserResponseDTO for the internal mapToDTOWithDetails call
        UserResponseDTO userResponse = new UserResponseDTO();
        UserResponseDTO.UserData userData = new UserResponseDTO.UserData();
        userData.setName("Smruti");
        userResponse.setData(userData);

        // 4. Stubbing
        when(reviewRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(reviewRepository.save(any(review.class))).thenReturn(updated);
        when(userClient.getUserById(100L)).thenReturn(userResponse);

        // 5. Execution
        reviewDTO result = service.updateReview(7L, request);

        // 6. Assertions
        assertEquals(7L, result.getId());
        assertEquals(4, result.getRating());
        assertEquals("Updated comment", result.getComment());
        assertEquals("Smruti", result.getUserName());
        verify(reviewRepository).findById(7L);
        verify(reviewRepository).save(any(review.class));
    }

    @Test
    void updateReviewThrowsWhenReviewDoesNotExist() {
        reviewUpdateDTO request = new reviewUpdateDTO();
        request.setRating(2);
        request.setComment("Missing");

        when(reviewRepository.findById(99L)).thenReturn(Optional.empty());

        ReviewNotFound exception = assertThrows(ReviewNotFound.class,
                () -> service.updateReview(99L, request));

        // Note: Capital 'R' to match your Service implementation
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository).findById(99L);
        verify(reviewRepository, never()).save(any(review.class));
    }

    @Test
    void deleteReviewDelegatesToRepository() {
        service.deleteReview(5L);
        verify(reviewRepository).deleteById(5L);
    }
}
