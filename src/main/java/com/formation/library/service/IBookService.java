package com.formation.library.service;

import java.time.LocalDate;
import java.util.List;

import com.formation.library.entity.Book;
import com.formation.library.entity.Book.BookStatus;

public interface IBookService {

  Book save(Book book);

  Book findById(Long id);

  Book findByIsbn(String isbn);

  List<Book> findAll();

  List<Book> findByTitle(String title);

  List<Book> findByStatus(BookStatus status);

  List<Book> findByGenre(String genre);

  List<Book> findByAuthorId(Long authorId);

  List<Book> findByAuthorName(String authorName);

  List<Book> findByPublicationDateBetween(LocalDate startDate, LocalDate endDate);

  Book update(Long id, Book book);

  void deleteById(Long id);

  Book borrowBook(String isbn);

  Book returnBook(String isbn);

  long countByAuthorAndStatus(Long authorId, BookStatus status);

  boolean existsByIsbn(String isbn);
}
