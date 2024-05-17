package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.payload.BorrowBookRequestDTO;
import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.service.LibraryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.librarymanagementsystem.util.ApplicationConstants.BORROW_BOOK_RESPONSE;
import static com.librarymanagementsystem.util.ApplicationConstants.RETURN_BOOK_RESPONSE;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class LibraryController {

    private LibraryService libraryService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<MessageResponseDTO> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId, @Valid @RequestBody BorrowBookRequestDTO borrowBookRequestDTO) {
        libraryService.borrowBook(bookId, patronId, borrowBookRequestDTO);
        return ResponseEntity.ok(new MessageResponseDTO(BORROW_BOOK_RESPONSE));

    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<MessageResponseDTO> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        libraryService.returnBook(bookId, patronId);
        return ResponseEntity.ok(new MessageResponseDTO(RETURN_BOOK_RESPONSE));
    }
}
