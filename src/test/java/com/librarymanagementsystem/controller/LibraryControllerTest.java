package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.payload.BorrowBookRequestDTO;
import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.service.LibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_Success() {
        BorrowBookRequestDTO requestDTO = new BorrowBookRequestDTO();
        requestDTO.setReturnDate(LocalDate.now().plusDays(7));

        ResponseEntity<MessageResponseDTO> responseEntity = libraryController.borrowBook(1L, 1L, requestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book borrowed Successfully!", responseEntity.getBody().getMessage());
    }

    @Test
    void returnBook_Success() {
        libraryService.borrowBook(1L,1L, new BorrowBookRequestDTO(LocalDate.now().plusDays(5)));

        ResponseEntity<MessageResponseDTO> responseEntity = libraryController.returnBook(1L, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book returned Successfully!", responseEntity.getBody().getMessage());
    }

    @Test
    void returnBook_Failure() {
        when(libraryService.returnBook(anyLong(), anyLong())).thenThrow(new ResourceNotFoundException("Book", "ID", 1L));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            libraryController.returnBook(1L, 1L);
        });

        assertEquals("Book not found with ID: 1", exception.getMessage());

    }
}


