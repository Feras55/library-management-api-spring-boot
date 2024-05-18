package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.payload.MessageResponseDTO;
import com.librarymanagementsystem.payload.PatronDTO;
import com.librarymanagementsystem.service.PatronService;
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

@AllArgsConstructor
@RestController
@RequestMapping("/api/patrons")
@Tag(name = "Patron")
public class PatronController {
    private PatronService patronService;

    @Operation(
            description = GET_ALL_PATRONS_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = OK
                    )
            })
    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        List<PatronDTO> patrons = patronService.getAllPatrons();
        return ResponseEntity.ok(patrons);
    }

    @Operation(
            description = GET_PATRON_BY_ID_DESCRIPTION,
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
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        PatronDTO patron = patronService.getPatronById(id);
        return ResponseEntity.ok(patron);
    }

    @Operation(
            description = ADD_PATRON_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = CREATED
                    ), @ApiResponse(
                    description = "Bad Request",
                    responseCode = BAD_REQUEST
            )
            })
    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@Valid @RequestBody PatronDTO patron) {
        PatronDTO savedPatron = patronService.addPatron(patron);
        return new ResponseEntity<>(savedPatron, HttpStatus.CREATED);
    }

    @Operation(
            description = UPDATE_PATRON_DESCRIPTION,
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
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronDTO patron) {
        PatronDTO updatedPatron = patronService.updatePatron(id, patron);
        return ResponseEntity.ok(updatedPatron);

    }

    @Operation(
            description = DELETE_PATRON_DESCRIPTION,
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
    public ResponseEntity<MessageResponseDTO> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.ok(new MessageResponseDTO(DELETE_PATRON_RESPONSE));

    }
}
