package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.payload.BorrowBookRequestDTO;
import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.service.LibraryService;
import com.librarymanagementsystem.util.ApplicationConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.librarymanagementsystem.util.ApplicationConstants.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "Library")
public class LibraryController {

    private LibraryService libraryService;

    @Operation(
            description = BORROW_BOOK_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = ApplicationConstants.OK
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = ApplicationConstants.BAD_REQUEST
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = NOT_FOUND
                    )
            })
    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<MessageResponseDTO> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId, @Valid @RequestBody BorrowBookRequestDTO borrowBookRequestDTO) {
        libraryService.borrowBook(bookId, patronId, borrowBookRequestDTO);
        return ResponseEntity.ok(new MessageResponseDTO(BORROW_BOOK_RESPONSE));

    }

    @Operation(
            description = RETURN_BOOK_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = ApplicationConstants.OK
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = NOT_FOUND
                    )
            })
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<MessageResponseDTO> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        libraryService.returnBook(bookId, patronId);
        return ResponseEntity.ok(new MessageResponseDTO(RETURN_BOOK_RESPONSE));
    }
}
