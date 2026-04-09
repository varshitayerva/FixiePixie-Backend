package com.example.reviews.utility;

public class ReviewNotFound extends RuntimeException {
    public ReviewNotFound(String message) {
        super(message);
    }
}
