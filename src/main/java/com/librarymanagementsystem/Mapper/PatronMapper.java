package com.librarymanagementsystem.Mapper;

import com.librarymanagementsystem.model.Patron;
import com.librarymanagementsystem.payload.PatronDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatronMapper {
    PatronMapper MAPPER = Mappers.getMapper(PatronMapper.class);

    PatronDTO mapToPatronDTO (Patron patron);
    Patron mapToPatron(PatronDTO patronDTO);
}
