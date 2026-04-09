package com.example.reviews;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewsApplicationTests {

    @Test
    void applicationClassHasSpringBootAnnotation() {
        assertTrue(ReviewsApplication.class.isAnnotationPresent(SpringBootApplication.class));
    }
}
