package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.payload.PatronDTO;
import com.librarymanagementsystem.service.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.librarymanagementsystem.util.ApplicationConstants.DELETE_PATRON_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
class PatronControllerTest {

    @MockBean
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPatrons_Success() {
        List<PatronDTO> patrons = Arrays.asList(
                new PatronDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new PatronDTO(2L, "Jane", "Doe", "jane.doe@example.com", "0987654321")
        );

        when(patronService.getAllPatrons()).thenReturn(patrons);

        ResponseEntity<List<PatronDTO>> response = patronController.getAllPatrons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patrons, response.getBody());
        verify(patronService, times(1)).getAllPatrons();
    }

    @Test
    void getPatronById_Success() {
        PatronDTO patron = new PatronDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        when(patronService.getPatronById(1L)).thenReturn(patron);

        ResponseEntity<PatronDTO> response = patronController.getPatronById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patron, response.getBody());
        verify(patronService, times(1)).getPatronById(1L);
    }

    @Test
    void getPatronById_NotFound() {
        when(patronService.getPatronById(1L)).thenThrow(new ResourceNotFoundException("Patron", "id", 1L));

        try {
            patronController.getPatronById(1L);
        } catch (ResourceNotFoundException ex) {
            assertEquals("Patron not found with id: 1", ex.getMessage());
            verify(patronService, times(1)).getPatronById(1L);
        }
    }

    @Test
    void addPatron_Success() {
        PatronDTO patron = new PatronDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        when(patronService.addPatron(any(PatronDTO.class))).thenReturn(patron);

        ResponseEntity<PatronDTO> response = patronController.addPatron(patron);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patron, response.getBody());
        verify(patronService, times(1)).addPatron(any(PatronDTO.class));
    }

    @Test
    void addPatron_AlreadyExists() {
        PatronDTO patron = new PatronDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        when(patronService.addPatron(any(PatronDTO.class))).thenThrow(new AlreadyExistsException("Patron", "email", "john.doe@example.com"));

        try {
            patronController.addPatron(patron);
        } catch (AlreadyExistsException ex) {
            assertEquals("Patron with email: 'john.doe@example.com' already exists", ex.getMessage());
        }
        verify(patronService, times(1)).addPatron(any(PatronDTO.class));
    }

    @Test
    void updatePatron_Success() {
        PatronDTO patron = new PatronDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        when(patronService.updatePatron(anyLong(), any(PatronDTO.class))).thenReturn(patron);

        ResponseEntity<PatronDTO> response = patronController.updatePatron(1L, patron);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patron, response.getBody());
        verify(patronService, times(1)).updatePatron(anyLong(), any(PatronDTO.class));
    }

    @Test
    void updatePatron_NotFound() {
        PatronDTO patron = new PatronDTO(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        when(patronService.updatePatron(anyLong(), any(PatronDTO.class))).thenThrow(new ResourceNotFoundException("Patron", "id", 1L));

        try {
            patronController.updatePatron(1L, patron);
        } catch (ResourceNotFoundException ex) {
            assertEquals("Patron not found with id: 1", ex.getMessage());
        }

        verify(patronService, times(1)).updatePatron(anyLong(), any(PatronDTO.class));
    }

    @Test
    void deletePatron_Success() {
        doNothing().when(patronService).deletePatron(anyLong());

        ResponseEntity<MessageResponseDTO> response = patronController.deletePatron(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DELETE_PATRON_RESPONSE, response.getBody().getMessage());
        verify(patronService, times(1)).deletePatron(anyLong());
    }

    @Test
    void deletePatron_NotFound() {
        doThrow(new ResourceNotFoundException("Patron", "id", 1L)).when(patronService).deletePatron(anyLong());

        try {
            patronController.deletePatron(1L);
        } catch (ResourceNotFoundException ex) {
            assertEquals("Patron not found with id: 1", ex.getMessage());
        }

        verify(patronService, times(1)).deletePatron(anyLong());
    }
}



