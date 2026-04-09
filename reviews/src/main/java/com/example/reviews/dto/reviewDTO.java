package com.example.reviews.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


@JsonInclude(JsonInclude.Include.NON_NULL)
public class reviewDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

//    @NotNull(message = "User Name is required")
    String userName;

    @NotNull(message = "Provider Service ID is required")
    @Positive(message = "Provider Service ID must be positive")
    private Long providerServiceId;


    private Long bookingId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @NotBlank(message = "Comment cannot be blank")
    @Size(max = 500, message = "Comment is too long")
    private String comment;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProviderServiceId() {
        return providerServiceId;
    }

    public void setProviderServiceId(Long providerServiceId) {
        this.providerServiceId = providerServiceId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}