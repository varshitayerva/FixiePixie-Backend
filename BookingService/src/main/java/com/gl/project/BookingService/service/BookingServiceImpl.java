package com.gl.project.BookingService.service;

import com.gl.project.BookingService.client.NotificationClient;
import com.gl.project.BookingService.client.ProviderClient;
import com.gl.project.BookingService.client.UserClient;
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
    private final UserClient userClient;
    private final ProviderClient providerClient;
    private final NotificationClient notificationClient;

//    @Override
//    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
//
//        String userResponse = userClient.getUserById(dto.getUserId());
//
//        String providerResponse = providerClient.getServiceById(dto.getServiceId());
//
//        if (userResponse == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        if (providerResponse == null) {
//            throw new RuntimeException("Service not found");
//        }
//
//        Booking booking = new Booking();
//        booking.setUserId(dto.getUserId());
//        booking.setServiceId(dto.getServiceId());
//        booking.setDate(dto.getDate());
//        booking.setStatus("CONFIRMED");
//        booking.setTimeSlot(dto.getTimeSlot());
//
//        Booking saved = bookingRepository.save(booking);
//
//        return new BookingResponseDTO(
//                saved.getId(),
//                saved.getUserId(),
//                saved.getServiceId(),
//                saved.getDate(),
//                saved.getStatus(),
//                saved.getTimeSlot()
//        );
//    }





    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        String userResponse = String.valueOf(userClient.getUserById(dto.getUserId()));
        String providerResponse = providerClient.getServiceById(dto.getServiceId());

        if (userResponse == null) throw new RuntimeException("User not found");
        if (providerResponse == null) throw new RuntimeException("Service not found");

        Booking booking = new Booking();
        booking.setUserId(dto.getUserId());
        booking.setServiceId(dto.getServiceId());
        booking.setDate(dto.getDate());
        // Start as PENDING_PAYMENT to trigger frontend redirect
        booking.setStatus("PENDING_PAYMENT");
        booking.setTimeSlot(dto.getTimeSlot());

        Booking saved = bookingRepository.save(booking);

        return new BookingResponseDTO(
                saved.getId(), saved.getUserId(), saved.getServiceId(),
                saved.getDate(), saved.getStatus(), saved.getTimeSlot()
        );
    }

    @Override
    public void updateBookingStatus(Long id, String status) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(status);
        bookingRepository.save(booking);

        UserResponseDTO response = userClient.getUserById(booking.getUserId());
        UserDTO user = response.getData();

        NotificationRequest request = new NotificationRequest();
        request.setEmail(user.getEmail());
        request.setPhoneNumber(String.valueOf(user.getNumber()));

        if(status.equals("CONFIRMED")) {

            request.setSubject("PixeFixe Booking Confirmed");

            request.setMessage(
                    "🎉 Payment Successful!\n\n" +
                            "Your booking is confirmed with PixeFixe.\n" +
                            "Booking ID: " + id + "\n" +
                            "Status: CONFIRMED\n\n" +
                            "Thank you for choosing PixeFixe.\n" +
                            "We are flying to fix your service! ✨"
            );
        }

        if(status.equals("CANCELLED")) {

            request.setSubject("PixeFixe Booking Cancelled");

            request.setMessage(
                    "❌ Payment Cancelled\n\n" +
                            "Your booking has been cancelled.\n" +
                            "Booking ID: " + id + "\n\n" +
                            "If any amount was deducted, it will be refunded.\n" +
                            "— PixeFixe Support"
            );
        }

        notificationClient.sendEmail(request);
        notificationClient.sendSms(request);
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