package com.example.reviews.repository;

import com.example.reviews.entity.review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reviewRepository extends JpaRepository<review, Long> {

}
