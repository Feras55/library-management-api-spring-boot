package com.librarymanagementsystem.config;

import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.model.Patron;
import com.librarymanagementsystem.repository.BookRepository;
import com.librarymanagementsystem.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public DataInitializer(BookRepository bookRepository, PatronRepository patronRepository) {
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            for (int i = 1; i <= 5; i++) {
                Patron patron = new Patron();
                patron.setFirstName("FirstName" + i);
                patron.setLastName("LastName" + i);
                patron.setEmail("patron" + i + "@example.com");
                patron.setPhoneNumber("123-456-789-0" + i);
                patronRepository.save(patron);
            }

            for (int i = 1; i <= 5; i++) {
                Book book = new Book();
                book.setTitle("Book Title " + i);
                book.setAuthor("Author " + i);
                book.setPublicationYear(2000 + i);
                book.setIsbn("ISBN" + i);
                bookRepository.save(book);
            }
        };
    }
}
