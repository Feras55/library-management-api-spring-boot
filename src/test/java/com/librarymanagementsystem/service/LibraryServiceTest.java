package com.librarymanagementsystem.service;

import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.Patron;
import com.librarymanagementsystem.payload.BorrowBookRequestDTO;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.LibraryRepository;
import com.librarymanagementsystem.repository.PatronRepository;
import com.librarymanagementsystem.service.impl.LibraryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_Success() {
        // Mock data
        Book book = new Book(1L, "Test Book", "Author", 2020, "1234567890", Set.of());
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "1234567890", Set.of());
        BorrowBookRequestDTO borrowBookRequestDTO = new BorrowBookRequestDTO(LocalDate.now().plusDays(7));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(libraryRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Optional.empty());

        // Test
        boolean result = libraryService.borrowBook(1L, 1L, borrowBookRequestDTO);

        // Verify
        assertTrue(result);
        verify(libraryRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_Failure_BookNotFound() {
        // Mock data
        BorrowBookRequestDTO borrowBookRequestDTO = new BorrowBookRequestDTO(LocalDate.now().plusDays(7));

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> libraryService.borrowBook(1L, 1L, borrowBookRequestDTO));
    }

    @Test
    void borrowBook_Failure_PatronNotFound() {
        // Mock data
        Book book = new Book(1L, "Test Book", "Author", 2020, "1234567890", Set.of());
        BorrowBookRequestDTO borrowBookRequestDTO = new BorrowBookRequestDTO(LocalDate.now().plusDays(7));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> libraryService.borrowBook(1L, 1L, borrowBookRequestDTO));
    }

    @Test
    void borrowBook_Failure_AlreadyBorrowed() {
        // Mock data
        Book book = new Book(1L, "Test Book", "Author", 2020, "1234567890", Set.of());
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "1234567890", Set.of());
        BorrowBookRequestDTO borrowBookRequestDTO = new BorrowBookRequestDTO(LocalDate.now().plusDays(7));
        BorrowingRecord borrowingRecord = new BorrowingRecord();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(libraryRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Optional.of(borrowingRecord));

        // Test and Verify
        assertThrows(AlreadyExistsException.class, () -> libraryService.borrowBook(1L, 1L, borrowBookRequestDTO));
    }

    @Test
    void returnBook_Success() {
        // Mock data
        Book book = new Book(1L, "Test Book", "Author", 2020, "1234567890", Set.of());
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "1234567890", Set.of());
        BorrowingRecord borrowingRecord = new BorrowingRecord();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(libraryRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Optional.of(borrowingRecord));

        // Test
        boolean result = libraryService.returnBook(1L, 1L);

        // Verify
        assertTrue(result);
        verify(libraryRepository, times(1)).delete(borrowingRecord);
    }

    @Test
    void returnBook_Failure_BookNotFound() {
        // Mock data
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> libraryService.returnBook(1L, 1L));
    }

    @Test
    void returnBook_Failure_PatronNotFound() {
        // Mock data
        Book book = new Book(1L, "Test Book", "Author", 2020, "1234567890", Set.of());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> libraryService.returnBook(1L, 1L));
    }

    @Test
    void returnBook_Failure_NotBorrowed() {
        // Mock data
        Book book = new Book(1L, "Test Book", "Author", 2020, "1234567890", Set.of());
        Patron patron = new Patron(1L, "John", "Doe", "john@example.com", "1234567890", Set.of());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(libraryRepository.findByBookIdAndPatronId(1L, 1L)).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(ResourceNotFoundException.class, () -> libraryService.returnBook(1L, 1L));
    }
}

