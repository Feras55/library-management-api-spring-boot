package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.payload.BookDTO;
import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.librarymanagementsystem.util.ApplicationConstants.*;
import static com.librarymanagementsystem.util.ApplicationConstants.BAD_REQUEST;
import static com.librarymanagementsystem.util.ApplicationConstants.CREATED;
import static com.librarymanagementsystem.util.ApplicationConstants.NOT_FOUND;
import static com.librarymanagementsystem.util.ApplicationConstants.OK;
import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/books")
@Tag(name = "Book")
public class BookController {

    private BookService bookService;

    @Operation(
            description = GET_ALL_BOOKS_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = OK
                    )
            })
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(
            description = GET_BOOK_BY_ID_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = OK
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = NOT_FOUND
                    )
            })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(
            description = ADD_BOOK_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = CREATED
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = BAD_REQUEST
                    )
            })
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO book) {
        BookDTO savedBook = bookService.addBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @Operation(
            description = UPDATE_BOOK_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = OK
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = NOT_FOUND
                    )
            })
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO book) {
        BookDTO updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(
            description = DELETE_BOOK_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = OK
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = NOT_FOUND
                    )
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new MessageResponseDTO(DELETE_BOOK_RESPONSE));

    }

}
