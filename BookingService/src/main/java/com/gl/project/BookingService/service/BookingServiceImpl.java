package com.gl.project.BookingService.service;

import com.gl.project.BookingService.dto.*;
import com.gl.project.BookingService.entity.Booking;
import com.gl.project.BookingService.repository.BookingRepository;
import com.gl.project.BookingService.service.BookingService;
import com.gl.project.BookingService.utility.BookingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) throws BookingException {

        Booking booking = modelMapper.map(dto, Booking.class);
        booking.setStatus("BOOKED");

        Booking saved = bookingRepository.save(booking);

        return modelMapper.map(saved, BookingResponseDTO.class);
    }

    @Override
    public List<BookingResponseDTO> getBookingsByUser(Long userId) throws BookingException {

        List<Booking> bookings = bookingRepository.findByUserId(userId);

        if (bookings.isEmpty()) {
            throw new BookingException("No bookings found for user");
        }

        return bookings.stream()
                .map(b -> modelMapper.map(b, BookingResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteBooking(Long id) throws BookingException {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return "Booking cancelled successfully";
    }
}