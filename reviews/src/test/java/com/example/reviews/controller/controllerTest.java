package com.example.reviews.controller;

import com.example.reviews.dto.reviewDTO;
import com.example.reviews.dto.reviewUpdateDTO;
import com.example.reviews.service.reviewService;
import com.example.reviews.utility.GlobalExceptionHandler;
import com.example.reviews.utility.ReviewNotFound;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controller.class)
@Import(GlobalExceptionHandler.class)
class controllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private reviewService reviewService;

    @Test
    void saveReviewReturnsCreatedReview() throws Exception {
        reviewDTO request = reviewDTO.builder()
                .userId(10L)
                .userName("Smruti")
                .providerServiceId(4L)
                .bookingId(9L)
                .rating(5)
                .comment("Very good")
                .build();

        reviewDTO response = reviewDTO.builder()
                .id(1L)
                .userId(10L)
                .userName("Smruti")
                .providerServiceId(4L)
                .bookingId(9L)
                .rating(5)
                .comment("Very good")
                .createdAt(LocalDateTime.of(2026, 4, 8, 12, 0))
                .build();

        when(reviewService.createReview(any(reviewDTO.class))).thenReturn(response);

        mockMvc.perform(post("/review/give")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(10L))
                .andExpect(jsonPath("$.userName").value("Smruti"))
                .andExpect(jsonPath("$.providerServiceId").value(4L))
                .andExpect(jsonPath("$.bookingId").value(9L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Very good"));
    }

    @Test
    void saveReviewReturnsBadRequestForValidationFailure() throws Exception {
        reviewDTO request = reviewDTO.builder()
                .userId(null)
                .userName(null)
                .providerServiceId(null)
                .bookingId(null)
                .rating(6)
                .comment("")
                .build();

        mockMvc.perform(post("/review/give")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors.userId").value("User ID is required"))
                .andExpect(jsonPath("$.validationErrors.userName").value("User Name is required"))
                .andExpect(jsonPath("$.validationErrors.providerServiceId").value("Provider Service ID is required"))
                .andExpect(jsonPath("$.validationErrors.bookingId").value("Booking ID is required"))
                .andExpect(jsonPath("$.validationErrors.rating").value("Rating cannot exceed 5"))
                .andExpect(jsonPath("$.validationErrors.comment").value("Comment cannot be blank"));
    }

    @Test
    void updateReviewReturnsNotFoundWhenServiceThrows() throws Exception {
        reviewUpdateDTO request = new reviewUpdateDTO();
        request.setRating(4);
        request.setComment("Updated");

        when(reviewService.updateReview(eq(77L), any(reviewUpdateDTO.class)))
                .thenThrow(new ReviewNotFound("review not found"));
    }}