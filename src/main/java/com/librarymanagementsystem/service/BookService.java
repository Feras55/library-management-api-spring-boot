package com.librarymanagementsystem.service;

import com.librarymanagementsystem.payload.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long id);

    BookDTO addBook(BookDTO book);

    BookDTO updateBook(Long id, BookDTO book);

    boolean deleteBook(Long id);
}
