package com.librarymanagementsystem.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.librarymanagementsystem.util.customValidation.FutureOrPresentDate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBookRequestDTO {
    @NotNull(message = "Return Date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @FutureOrPresentDate
    private LocalDate returnDate;
}
