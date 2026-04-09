package com.gl.project.BookingService.service;

import com.gl.project.BookingService.dto.*;
import com.gl.project.BookingService.utility.BookingException;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO dto) throws BookingException;

    List<BookingResponseDTO> getBookingsByUser(Long userId) throws BookingException;

    String deleteBooking(Long id) throws BookingException;

    void updateBookingStatus(Long id, String status);
}