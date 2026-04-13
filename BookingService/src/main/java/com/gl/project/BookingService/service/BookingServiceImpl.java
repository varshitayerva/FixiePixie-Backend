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




    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        UserResponseDTO userResponse = userClient.getUserById(dto.getUserId());
        if (userResponse == null || userResponse.getData() == null) {
            throw new RuntimeException("User not found");
        }

        ServiceDTO serviceResponse = providerClient.getServiceById(dto.getServiceId());
        if (serviceResponse == null) {
            throw new RuntimeException("Service not found");
        }
        Booking booking = new Booking();
        booking.setUserId(dto.getUserId());
        booking.setServiceId(dto.getServiceId());
        booking.setDate(dto.getDate());
        booking.setStatus("PENDING_PAYMENT");
        booking.setTimeSlot(dto.getTimeSlot());

        Booking saved = bookingRepository.save(booking);

        // Use the Builder instead of the constructor to avoid the "Cannot resolve constructor" error
        return BookingResponseDTO.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .serviceId(saved.getServiceId())
                .date(saved.getDate())
                .status(saved.getStatus())
                .timeSlot(saved.getTimeSlot())
                .build();
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
    public List<BookingDetailsDTO> getBookingsByUser(Long userId) throws BookingException {

        List<Booking> bookings = bookingRepository.findByUserId(userId);

        if (bookings.isEmpty()) {
            throw new BookingException("No bookings found for user");
        }

        return bookings.stream().map(booking -> {

            UserResponseDTO userResponse = userClient.getUserById(booking.getUserId());
            String userName = userResponse.getData().getName();

            ServiceDTO service = providerClient.getServiceById(booking.getServiceId());
            String serviceName = service.getServiceName();



            return new BookingDetailsDTO(
                    booking.getId(),
                    userName,
                    serviceName,
                    booking.getServiceId(),
                    booking.getDate(),
                    booking.getStatus(),
                    booking.getTimeSlot()
            );

        }).collect(Collectors.toList());
    }

    @Override
    public List<BookingDetailsDTO> getBookingsByServiceProvider(Long serviceId) throws BookingException {

        List<Booking> bookings = bookingRepository.findByServiceId(serviceId);

        if (bookings.isEmpty()) {
            throw new BookingException("No bookings found for this service");
        }

        ServiceDTO service = providerClient.getServiceById(serviceId);
        String serviceName = service.getServiceName();

        return bookings.stream().map(booking -> {

            UserResponseDTO userResponse = userClient.getUserById(booking.getUserId());
            String userName = userResponse.getData().getName();

            return new BookingDetailsDTO(
                    booking.getId(),
                    userName,
                    serviceName,
                    booking.getServiceId(),
                    booking.getDate(),
                    booking.getStatus(),
                    booking.getTimeSlot()
            );

        }).collect(Collectors.toList());
    }

    @Override
    public String deleteBooking(Long id) throws BookingException {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        return "Booking cancelled successfully";
    }

    public List<BookingResponseDTO> getProviderDashboard(Long providerId) {
        // 1. Get all services owned by this provider from ProviderService
        List<ServiceDTO> myServices = providerClient.getServicesByProvider(providerId);

        // Extract just the IDs to query the Booking DB
        List<Long> serviceIds = myServices.stream()
                .map(ServiceDTO::getId)
                .toList();

        // 2. Find all bookings in our DB for these services
        List<Booking> bookings = bookingRepository.findByServiceIdIn(serviceIds);

        // 3. Transform Bookings into ResponseDTOs with Customer/Service info
        return bookings.stream().map(booking -> {
            // Find the service name from our list
            String sName = myServices.stream()
                    .filter(s -> s.getId().equals(booking.getServiceId()))
                    .findFirst()
                    .map(ServiceDTO::getServiceName)
                    .orElse("Unknown Service");

            // Call User Service to get the Customer's name and number
            // (Assuming your UserDTO has name, number, address)
            UserResponseDTO response = userClient.getUserById(booking.getUserId());
            UserDTO customer = response.getData();

            return BookingResponseDTO.builder()
                    .id(booking.getId())
                    .userId(booking.getUserId())
                    .serviceId(booking.getServiceId())
                    .date(booking.getDate())
                    .status(booking.getStatus())
                    .timeSlot(booking.getTimeSlot())
                    .serviceName(sName)
                    .customerName(customer.getName())
                    .customerNumber(String.valueOf(customer.getNumber()))
                    .customerAddress(customer.getAddress())
                    .build();
        }).collect(Collectors.toList());
    }
}