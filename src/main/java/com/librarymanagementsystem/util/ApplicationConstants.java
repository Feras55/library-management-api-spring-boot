package com.librarymanagementsystem.util;

import org.springframework.http.HttpStatus;

public class ApplicationConstants {

    public static String DELETE_BOOK_RESPONSE = "Book Successfully Deleted!";
    public static String DELETE_PATRON_RESPONSE = "Patron Successfully Deleted!";
    public static String BORROW_BOOK_RESPONSE = "Book borrowed Successfully!";
    public static String RETURN_BOOK_RESPONSE = "Book returned Successfully!";
    public static String BOOK = "Book";
    public static String ID = "ID";
    public static String ISBN = "ISBN";
    public static String PATRON = "Patron";
    public static String EMAIL = "Email";

    public static String BOOK_ALREADY_BORROWED = "Book is already borrowed by this patron";
    public static String BOOK_NOT_BORROWED = "Book is not borrowed by this patron";

    public static final String GET_ALL_BOOKS_DESCRIPTION = "Retrieve all books";
    public static final String GET_BOOK_BY_ID_DESCRIPTION = "Retrieve book by ID";
    public static final String ADD_BOOK_DESCRIPTION = "Add new Book";
    public static final String UPDATE_BOOK_DESCRIPTION = "Update existing Book";
    public static final String DELETE_BOOK_DESCRIPTION = "Delete Book";
    public static final String GET_ALL_PATRONS_DESCRIPTION = "Retrieve all patrons";
    public static final String GET_PATRON_BY_ID_DESCRIPTION = "Retrieve patrons by ID";
    public static final String ADD_PATRON_DESCRIPTION = "Add new patron";
    public static final String UPDATE_PATRON_DESCRIPTION = "Update existing patron";
    public static final String DELETE_PATRON_DESCRIPTION = "Delete patron";
    public static final String BORROW_BOOK_DESCRIPTION = "Borrow Book";
    public static final String RETURN_BOOK_DESCRIPTION = "Return Book";

    public static final String CREATED = "201";
    public static final String BAD_REQUEST = "400";
    public static final String NOT_FOUND = "404";
    public static final String OK = "200";


}
