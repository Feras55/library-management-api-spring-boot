package com.librarymanagementsystem.service;

import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.payload.BookDTO;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;


    @Test
    public void getAllBooks_Success() {

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Title1", "Author1", 2022, "ISBN1", new HashSet<>()));
        books.add(new Book(2L, "Title2", "Author2", 2023, "ISBN2", new HashSet<>()));
        when(bookRepository.findAll()).thenReturn(books);


        List<BookDTO> result = bookService.getAllBooks();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(books.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(books.get(1).getTitle(), result.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void getBookById_Success() {

        Long id = 1L;
        Book book = new Book(id, "Title", "Author", 2022, "ISBN", new HashSet<>());
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));


        BookDTO result = bookService.getBookById(id);


        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    public void getBookById_Failure_NotFound() {

        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(id));
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    public void addBook_Success() {

        BookDTO bookDTO = new BookDTO(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780743273565");
        Book savedBook = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780743273565", new HashSet<>());
        when(bookRepository.findByIsbn("ISBN")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);


        BookDTO result = bookService.addBook(bookDTO);


        assertNotNull(result);
        assertEquals(savedBook.getId(), result.getId());
        assertEquals(savedBook.getTitle(), result.getTitle());
        assertEquals(savedBook.getAuthor(), result.getAuthor());
        assertEquals(savedBook.getPublicationYear(), result.getPublicationYear());
        assertEquals(savedBook.getIsbn(), result.getIsbn());
        verify(bookRepository, times(1)).findByIsbn("9780743273565");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void addBook_Failure_AlreadyExists() {

        BookDTO bookDTO = new BookDTO(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "9780743273565");
        when(bookRepository.findByIsbn("9780743273565")).thenReturn(Optional.of(new Book()));


        assertThrows(AlreadyExistsException.class, () -> bookService.addBook(bookDTO));
        verify(bookRepository, times(1)).findByIsbn("9780743273565");
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void updateBook_Success() {

        Long id = 1L;
        Book existingBook = new Book(id, "Title", "Author", 2022, "ISBN", new HashSet<>());
        Book updatedBook = new Book(id, "New Title", "New Author", 2023, "New ISBN", new HashSet<>());
        BookDTO bookDTO = new BookDTO(id, "New Title", "New Author", 2023, "New ISBN");

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);


        BookDTO result = bookService.updateBook(id, bookDTO);


        assertNotNull(result);
        assertEquals(updatedBook.getTitle(), result.getTitle());
        assertEquals(updatedBook.getAuthor(), result.getAuthor());
        assertEquals(updatedBook.getPublicationYear(), result.getPublicationYear());
        assertEquals(updatedBook.getIsbn(), result.getIsbn());
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void updateBook_Failure_NotFound() {

        Long id = 1L;
        BookDTO bookDTO = new BookDTO(id, "New Title", "New Author", 2023, "New ISBN");

        when(bookRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(id, bookDTO));
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, never()).save(any());
    }

    @Test
    public void deleteBook_Success() {

        Long id = 1L;

        when(bookRepository.existsById(id)).thenReturn(true);


        bookService.deleteBook(id);


        verify(bookRepository, times(1)).existsById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteBook_Failure_NotFound() {

        Long id = 1L;

        when(bookRepository.existsById(id)).thenReturn(false);


        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(id));
        verify(bookRepository, times(1)).existsById(id);
        verify(bookRepository, never()).deleteById(any());
    }
}
