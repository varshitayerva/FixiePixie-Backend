package com.gl.project.BookingService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gl.project.BookingService.dto.*;
import com.gl.project.BookingService.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // FIXED TEST
    @Test
    void createBooking() throws Exception {

        BookingRequestDTO request = new BookingRequestDTO();
        request.setUserId(1L);
        request.setServiceId(10L);
        request.setDate(LocalDate.now().plusDays(1)); // FIX (future date)
        request.setTimeSlot("10AM");

        BookingResponseDTO response = new BookingResponseDTO(
                1L,
                1L,
                10L,
                LocalDate.now().plusDays(1),
                "PENDING_PAYMENT",
                "10AM"
        );

        when(bookingService.createBooking(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookingStatus() throws Exception {
        mockMvc.perform(put("/bookings/1/status?status=CONFIRMED"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByUser() throws Exception {

        BookingDetailsDTO dto = new BookingDetailsDTO(
                1L,
                "Sai",
                "Cleaning",
                LocalDate.now().plusDays(1),
                "CONFIRMED",
                "10AM"
        );

        when(bookingService.getBookingsByUser(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByServiceProvider() throws Exception {

        BookingDetailsDTO dto = new BookingDetailsDTO(
                1L,
                "Sai",
                "Cleaning",
                LocalDate.now().plusDays(1),
                "CONFIRMED",
                "10AM"
        );

        when(bookingService.getBookingsByServiceProvider(10L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings/service/10"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBooking() throws Exception {

        when(bookingService.deleteBooking(1L))
                .thenReturn("Booking cancelled successfully");

        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isOk());
    }
}