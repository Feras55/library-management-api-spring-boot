package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.Mapper.BookMapper;
import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.payload.BookDTO;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.librarymanagementsystem.util.ApplicationConstants.*;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookMapper.MAPPER::mapToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOOK, ID, id)
        );
        return BookMapper.MAPPER.mapToBookDTO(book);
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        bookRepository.findByIsbn(bookDTO.getIsbn()).ifPresent(book -> {
            throw new AlreadyExistsException(ISBN, book.getIsbn());
        });
        Book book = BookMapper.MAPPER.mapToBook(bookDTO);
        Book savedBook = bookRepository.save(book);
        return BookMapper.MAPPER.mapToBookDTO(savedBook);
    }

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

    @Override
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            throw new ResourceNotFoundException(BOOK, ID, id);
        }
    }
}
