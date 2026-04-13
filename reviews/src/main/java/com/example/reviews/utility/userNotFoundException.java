package com.example.reviews.utility;

public class userNotFoundException extends RuntimeException {
    public userNotFoundException(String message) {
        super(message);
    }
}
