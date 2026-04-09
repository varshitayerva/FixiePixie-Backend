package com.example.reviews.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

private String userName;



    @Column(nullable = false)
    private Long providerServiceId;



    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getProviderServiceId() {
        return providerServiceId;
    }

    public void setProviderServiceId(Long providerServiceId) {
        this.providerServiceId = providerServiceId;
    }
}