package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.Mapper.BookMapper;
import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.payload.BookDTO;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.librarymanagementsystem.util.ApplicationConstants.*;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Cacheable("books")
    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper.MAPPER::mapToBookDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "books", key = "#id")
    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOOK, ID, id)
        );
        return BookMapper.MAPPER.mapToBookDTO(book);
    }

    @CachePut(value = "books", key = "#bookDTO.isbn")
    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        bookRepository.findByIsbn(bookDTO.getIsbn()).ifPresent(book -> {
            throw new AlreadyExistsException(BOOK,ISBN, book.getIsbn());
        });
        Book book = BookMapper.MAPPER.mapToBook(bookDTO);
        Book savedBook = bookRepository.save(book);
        return BookMapper.MAPPER.mapToBookDTO(savedBook);
    }
    @CachePut(value = "books", key = "#id")
    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOOK, ID, id)
        );
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setIsbn(bookDTO.getIsbn());

        Book updatedBook = bookRepository.save(existingBook);
        return BookMapper.MAPPER.mapToBookDTO(updatedBook);
    }

    @CacheEvict(value = "books", key = "#id")

    @Override
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(BOOK, ID, id);
        }
    }
}
