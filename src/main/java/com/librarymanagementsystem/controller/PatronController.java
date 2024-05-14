package com.librarymanagementsystem.controller;

import com.librarymanagementsystem.payload.PatronDTO;
import com.librarymanagementsystem.service.PatronService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.librarymanagementsystem.util.ApplicationConstants.DELETE_PATRON_RESPONSE;

@AllArgsConstructor
@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        List<PatronDTO> patrons = patronService.getAllPatrons();
        return ResponseEntity.ok(patrons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        PatronDTO patron = patronService.getPatronById(id);
        return ResponseEntity.ok(patron);
    }

    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@Valid @RequestBody PatronDTO patron) {
        PatronDTO savedPatron = patronService.addPatron(patron);
        return new ResponseEntity<>(savedPatron, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronDTO patron) {
        PatronDTO updatedPatron = patronService.updatePatron(id, patron);
        return ResponseEntity.ok(updatedPatron);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatron(@PathVariable Long id) {
        boolean deleted = patronService.deletePatron(id);
        return ResponseEntity.ok(DELETE_PATRON_RESPONSE);

    }
}
