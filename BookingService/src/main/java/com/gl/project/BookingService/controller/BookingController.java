package com.gl.project.BookingService.controller;

import com.gl.project.BookingService.dto.*;
import com.gl.project.BookingService.service.BookingService;
import com.gl.project.BookingService.utility.BookingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

//    @PostMapping
//    public ResponseEntity<BookingResponseDTO> createBooking(
//            @Valid @RequestBody BookingRequestDTO dto) throws BookingException {
//
//        return ResponseEntity.ok(bookingService.createBooking(dto));
//    }
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUser(
//            @PathVariable Long userId) throws BookingException {
//
//        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteBooking(
//            @PathVariable Long id) throws BookingException {
//
//        return ResponseEntity.ok(bookingService.deleteBooking(id));
//    }


    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO dto) throws BookingException {
        // This will now return a booking with status "PENDING_PAYMENT"
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    // NEW ENDPOINT: Added for Payment Service automation
    // Path: PATCH /bookings/{id}/status?status=CONFIRMED
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsByUser(
            @PathVariable Long userId) throws BookingException {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<BookingDetailsDTO>> getBookingsByServiceProvider(
            @PathVariable Long serviceId) throws BookingException {

        return ResponseEntity.ok(
                bookingService.getBookingsByServiceProvider(serviceId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(
            @PathVariable Long id) throws BookingException {
        return ResponseEntity.ok(bookingService.deleteBooking(id));
    }

    @GetMapping("/provider-view/{providerId}")
    public ResponseEntity<List<BookingResponseDTO>> getProviderBookings(@PathVariable Long providerId) {
        List<BookingResponseDTO> dashboardData = bookingService.getProviderDashboard(providerId);

        if (dashboardData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dashboardData);
    }

}