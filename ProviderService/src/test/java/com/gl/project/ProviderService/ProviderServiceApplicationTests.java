package com.gl.project.ProviderService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.project.ProviderService.controller.ServiceController;
import com.gl.project.ProviderService.dto.ServiceDTO;
import com.gl.project.ProviderService.service.ServiceServiceInterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ServiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceServiceInterface service;

    @InjectMocks
    private ServiceController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void testAddService() throws Exception {

        ServiceDTO dto = new ServiceDTO(1L, "Cleaning", 500.0);

        Mockito.when(service.addService(Mockito.any())).thenReturn(dto);

        mockMvc.perform(post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("Cleaning"));
    }

    @Test
    void testGetServiceById() throws Exception {

        ServiceDTO dto = new ServiceDTO(1L, "Repair", 300.0);

        Mockito.when(service.getServiceById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/services/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("Repair"));
    }


    @Test
    void testGetAllServices() throws Exception {

        ServiceDTO dto = new ServiceDTO(1L, "Plumbing", 400.0);

        Mockito.when(service.getAllServices()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serviceName").value("Plumbing"));
    }


    @Test
    void testUpdateService() throws Exception {

        ServiceDTO dto = new ServiceDTO(1L, "Painting", 800.0);

        Mockito.when(service.updateService(Mockito.eq(1L), Mockito.any()))
                .thenReturn(dto);

        mockMvc.perform(put("/api/services/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("Painting"));
    }


    @Test
    void testDeleteService() throws Exception {

        Mockito.doNothing().when(service).deleteService(1L);

        mockMvc.perform(delete("/api/services/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service deleted successfully with ID: 1"));
    }
}
