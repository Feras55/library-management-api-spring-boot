package com.librarymanagementsystem.service;

import com.librarymanagementsystem.Mapper.PatronMapper;
import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Patron;
import com.librarymanagementsystem.payload.PatronDTO;
import com.librarymanagementsystem.repository.PatronRepository;
import com.librarymanagementsystem.service.impl.PatronServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronServiceImpl patronService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPatrons_Success() {
        // Mock data
        List<Patron> patrons = new ArrayList<>();
        patrons.add(new Patron(1L, "John", "Doe", "john@example.com", "1234567890", new HashSet<>()));
        when(patronRepository.findAll()).thenReturn(patrons);

        // Test
        List<PatronDTO> patronDTOs = patronService.getAllPatrons();

        // Verify
        assertNotNull(patronDTOs);
        assertEquals(1, patronDTOs.size());
        assertEquals("John", patronDTOs.get(0).getFirstName());
    }

    @Test
    void getPatronById_Success() {
        // Mock data
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "1234567890", new HashSet<>());
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        // Test
        PatronDTO patronDTO = patronService.getPatronById(1L);

        // Verify
        assertNotNull(patronDTO);
        assertEquals("John", patronDTO.getFirstName());
    }

    @Test
    void addPatron_Success() {
        // Mock data
        PatronDTO patronDTO = new PatronDTO(1L, "John", "Doe", "john@example.com", "1234567890");
        Patron patron = PatronMapper.MAPPER.mapToPatron(patronDTO);
        when(patronRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(patronRepository.save(any(Patron.class))).thenReturn(patron);

        // Test
        PatronDTO savedPatronDTO = patronService.addPatron(patronDTO);

        // Verify
        assertNotNull(savedPatronDTO);
        assertEquals("John", savedPatronDTO.getFirstName());
    }

    @Test
    void updatePatron_Success() {
        // Mock data
        PatronDTO patronDTO = new PatronDTO(1L, "John", "Doe", "john@example.com", "1234567890");
        Patron existingPatron = new Patron(1L, "Jane", "Doe", "jane@example.com", "9876543210", new HashSet<>());
        Patron updatedPatron = new Patron(1L, "John", "Doe", "john@example.com", "1234567890", new HashSet<>());
        when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any(Patron.class))).thenReturn(updatedPatron);

        // Test
        PatronDTO updatedPatronDTO = patronService.updatePatron(1L, patronDTO);

        // Verify
        assertNotNull(updatedPatronDTO);
        assertEquals("John", updatedPatronDTO.getFirstName());
    }

    @Test
    void deletePatron_Success() {
        // Mock data
        when(patronRepository.existsById(1L)).thenReturn(true);

        // Test
        assertDoesNotThrow(() -> patronService.deletePatron(1L));
    }

    @Test
    void addPatron_Failure_AlreadyExists() {
        // Mock data
        PatronDTO patronDTO = new PatronDTO(1L, "John", "Doe", "john@example.com", "1234567890");
        when(patronRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new Patron()));

        // Test and Verify
        assertThrows(AlreadyExistsException.class, () -> patronService.addPatron(patronDTO));
    }

    @Test
    void getPatronById_Failure_ResourceNotFound() {
        // Mock data
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> patronService.getPatronById(1L));
    }

    @Test
    void updatePatron_Failure_ResourceNotFound() {
        // Mock data
        PatronDTO patronDTO = new PatronDTO(1L, "John", "Doe", "john@example.com", "1234567890");
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> patronService.updatePatron(1L, patronDTO));
    }

    @Test
    void deletePatron_Failure_ResourceNotFound() {
        // Mock data
        when(patronRepository.existsById(1L)).thenReturn(false);

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> patronService.deletePatron(1L));
    }
}

