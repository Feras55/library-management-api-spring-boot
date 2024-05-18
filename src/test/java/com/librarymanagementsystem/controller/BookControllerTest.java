package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.payload.BookDTO;
import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.service.BookService;
import com.librarymanagementsystem.util.ApplicationConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_Success() {
        // Mock data
        BookDTO book1 = new BookDTO(1L, "Book1", "Author1", 2020, "1234567890");
        BookDTO book2 = new BookDTO(2L, "Book2", "Author2", 2021, "0987654321");
        List<BookDTO> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        // Test
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void getBookById_Success() {
        // Mock data
        BookDTO book = new BookDTO(1L, "Book1", "Author1", 2020, "1234567890");

        when(bookService.getBookById(anyLong())).thenReturn(book);

        // Test
        ResponseEntity<BookDTO> response = bookController.getBookById(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void getBookById_Failure() {
        when(bookService.getBookById(anyLong())).thenThrow(new ResourceNotFoundException("Book", "ID", 1L));

        // Test
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookController.getBookById(1L);
        });

        // Verify
        assertEquals("Book not found with ID: 1", exception.getMessage());
    }

    @Test
    void addBook_Success() {
        // Mock data
        BookDTO book = new BookDTO(1L, "Book1", "Author1", 2020, "1234567890");

        when(bookService.addBook(any(BookDTO.class))).thenReturn(book);

        // Test
        ResponseEntity<BookDTO> response = bookController.addBook(book);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void addBook_Failure() {
        // Mock data
        BookDTO book = new BookDTO(1L, "Book1", "Author1", 2020, "1234567890");

        when(bookService.addBook(any(BookDTO.class))).thenThrow(new AlreadyExistsException("Book", "ISBN", "1234567890"));

        // Test
        Exception exception = assertThrows(AlreadyExistsException.class, () -> {
            bookController.addBook(book);
        });

        // Verify
        assertEquals("Book with ISBN: '1234567890' already exists", exception.getMessage());
    }

    @Test
    void updateBook_Success() {
        // Mock data
        BookDTO book = new BookDTO(1L, "UpdatedBook", "UpdatedAuthor", 2021, "1234567890");

        when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenReturn(book);

        // Test
        ResponseEntity<BookDTO> response = bookController.updateBook(1L, book);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void updateBook_Failure() {
        // Mock data
        BookDTO book = new BookDTO(1L, "UpdatedBook", "UpdatedAuthor", 2021, "1234567890");

        when(bookService.updateBook(anyLong(), any(BookDTO.class))).thenThrow(new ResourceNotFoundException("Book", "ID", 1L));

        // Test
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookController.updateBook(1L, book);
        });

        // Verify
        assertEquals("Book not found with ID: 1", exception.getMessage());
    }

    @Test
    void deleteBook_Success() {
        // Mock data
        MessageResponseDTO responseMessage = new MessageResponseDTO(ApplicationConstants.DELETE_BOOK_RESPONSE);

        doNothing().when(bookService).deleteBook(anyLong());

        // Test
        ResponseEntity<MessageResponseDTO> response = bookController.deleteBook(1L);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }

    @Test
    void deleteBook_Failure() {
        doThrow(new ResourceNotFoundException("Book", "ID", 1L)).when(bookService).deleteBook(anyLong());

        // Test
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookController.deleteBook(1L);
        });

        // Verify
        assertEquals("Book not found with ID: 1", exception.getMessage());
    }
}
