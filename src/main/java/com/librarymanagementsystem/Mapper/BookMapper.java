package com.librarymanagementsystem.Mapper;

import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.payload.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper MAPPER = Mappers.getMapper(BookMapper.class);

    BookDTO mapToBookDTO(Book book);

    Book mapToBook(BookDTO bookDTO);
}
