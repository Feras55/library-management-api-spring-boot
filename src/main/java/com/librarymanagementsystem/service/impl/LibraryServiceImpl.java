package com.librarymanagementsystem.service.impl;

import com.librarymanagementsystem.exception.AlreadyExistsException;
import com.librarymanagementsystem.exception.InvalidDateException;
import com.librarymanagementsystem.exception.ResourceNotFoundException;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.BorrowingRecord;
import com.librarymanagementsystem.model.Patron;
import com.librarymanagementsystem.payload.BorrowBookRequestDTO;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.LibraryRepository;
import com.librarymanagementsystem.repository.PatronRepository;
import com.librarymanagementsystem.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.librarymanagementsystem.util.ApplicationConstants.*;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryServiceImpl(BookRepository bookRepository, PatronRepository patronRepository, LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.libraryRepository = libraryRepository;
    }

    @CachePut(value = "borrowedBooks", key = "#bookId" + '-' + "#patronId")
    @Override
    public boolean borrowBook(Long bookId, Long patronId, BorrowBookRequestDTO borrowBookRequestDTO) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException(BOOK, ID, bookId));
        Patron patron = patronRepository.findById(patronId).orElseThrow(
                () -> new ResourceNotFoundException(PATRON, ID, patronId));

        if (libraryRepository.findByBookIdAndPatronId(bookId, patronId).isPresent()) {
            throw new AlreadyExistsException(BOOK_ALREADY_BORROWED);
        }

        if(borrowBookRequestDTO.getReturnDate().isAfter(LocalDate.now())){
            throw new InvalidDateException();
        }
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecord.setReturnDate(borrowBookRequestDTO.getReturnDate());
        libraryRepository.save(borrowingRecord);
        return true;
    }

    @CacheEvict(value = "borrowedBooks", key = "#bookId" + '-' + "#patronId")
    @Override
    public boolean returnBook(Long bookId, Long patronId) {
        bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException(BOOK, ID, bookId));
        patronRepository.findById(patronId).orElseThrow(
                () -> new ResourceNotFoundException(PATRON, ID, patronId));
        BorrowingRecord borrowingRecord = libraryRepository.findByBookIdAndPatronId(bookId, patronId)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_BORROWED));
        libraryRepository.delete(borrowingRecord);
        return true;
    }
}
