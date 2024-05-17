package com.librarymanagementsystem.service;

import com.librarymanagementsystem.payload.BorrowBookRequestDTO;

public interface LibraryService {
   boolean borrowBook(Long bookId, Long patronId, BorrowBookRequestDTO borrowBookRequestDTO);
   boolean returnBook(Long bookId, Long patronId);
}
