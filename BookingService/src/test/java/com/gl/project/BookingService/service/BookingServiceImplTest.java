package com.gl.project.BookingService.service;

import com.gl.project.BookingService.client.NotificationClient;
import com.gl.project.BookingService.client.ProviderClient;
import com.gl.project.BookingService.client.UserClient;
import com.gl.project.BookingService.dto.*;
import com.gl.project.BookingService.entity.Booking;
import com.gl.project.BookingService.repository.BookingRepository;
import com.gl.project.BookingService.utility.BookingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserClient userClient;

    @Mock
    private ProviderClient providerClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private BookingServiceImpl bookingService;

    // TEST 1 — create booking
    @Test
    void createBooking_shouldSaveBooking() {

        BookingRequestDTO dto = new BookingRequestDTO();
        dto.setUserId(1L);
        dto.setServiceId(10L);
        dto.setDate(LocalDate.now());
        dto.setTimeSlot("10AM");

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Sai");

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setData(userDTO);

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceName("Cleaning");

        when(userClient.getUserById(1L)).thenReturn(userResponse);
        when(providerClient.getServiceById(10L)).thenReturn(serviceDTO);

        Booking saved = new Booking();
        saved.setId(1L);
        saved.setUserId(1L);
        saved.setServiceId(10L);
        saved.setStatus("PENDING_PAYMENT");

        when(bookingRepository.save(any())).thenReturn(saved);

        BookingResponseDTO response = bookingService.createBooking(dto);

        assertNotNull(response);
        assertEquals("PENDING_PAYMENT", response.getStatus());
    }

    // TEST 2 — get bookings by user
    @Test
    void getBookingsByUser_shouldReturnList() throws BookingException {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUserId(1L);
        booking.setServiceId(10L);
        booking.setDate(LocalDate.now());
        booking.setStatus("CONFIRMED");

        when(bookingRepository.findByUserId(1L))
                .thenReturn(List.of(booking));

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Sai");

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setData(userDTO);

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setServiceName("Cleaning");

        when(userClient.getUserById(1L)).thenReturn(userResponse);
        when(providerClient.getServiceById(10L)).thenReturn(serviceDTO);

        List<BookingDetailsDTO> result =
                bookingService.getBookingsByUser(1L);

        assertFalse(result.isEmpty());
    }

    // TEST 3 — update booking status
    @Test
    void updateBookingStatus_shouldUpdate() {

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUserId(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@mail.com");
        userDTO.setNumber(String.valueOf(9999999999L));

        UserResponseDTO response = new UserResponseDTO();
        response.setData(userDTO);

        when(userClient.getUserById(1L)).thenReturn(response);

        bookingService.updateBookingStatus(1L, "CONFIRMED");

        verify(bookingRepository).save(booking);
        verify(notificationClient).sendEmail(any());
    }

    // TEST 4 — delete booking
    @Test
    void deleteBooking_shouldCancelBooking() throws BookingException {

        Booking booking = new Booking();
        booking.setId(1L);

        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        String result = bookingService.deleteBooking(1L);

        assertEquals("Booking cancelled successfully", result);
    }
}