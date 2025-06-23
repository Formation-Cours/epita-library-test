package com.formation.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.library.entity.Book;
import com.formation.library.entity.Book.BookStatus;
import com.formation.library.exception.BookAlreadyBorrowedException;
import com.formation.library.exception.BookNotFoundException;
import com.formation.library.exception.InvalidIsbnException;
import com.formation.library.repository.BookRepository;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements IBookService {

  private final BookRepository bookRepository;

  public BookServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  @Transactional(readOnly = false)
  public Book save(Book book) {
    validateBook(book);
    return bookRepository.save(book);
  }

  @Override
  public Book findById(Long id) {
    return bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException(id));
  }

  @Override
  public Book findByIsbn(String isbn) {
    return bookRepository.findByIsbn(isbn)
        .orElseThrow(() -> new BookNotFoundException(isbn));
  }

  @Override
  public List<Book> findAll() {
    return bookRepository.findAll();
  }

  @Override
  public List<Book> findByTitle(String title) {
    return bookRepository.findByTitleContainingIgnoreCase(title);
  }

  @Override
  public List<Book> findByStatus(BookStatus status) {
    return bookRepository.findByStatus(status);
  }

  @Override
  public List<Book> findByGenre(String genre) {
    return bookRepository.findByGenreIgnoreCase(genre);
  }

  @Override
  public List<Book> findByAuthorId(Long authorId) {
    return bookRepository.findByAuthorId(authorId);
  }

  @Override
  public List<Book> findByAuthorName(String authorName) {
    return bookRepository.findByAuthorNameContaining(authorName);
  }

  @Override
  public List<Book> findByPublicationDateBetween(LocalDate startDate, LocalDate endDate) {
    return bookRepository.findByPublicationDateBetween(startDate, endDate);
  }

  @Override
  @Transactional(readOnly = false)
  public Book update(Long id, Book book) {
    Book existingBook = findById(id);

    existingBook.setTitle(book.getTitle());
    existingBook.setIsbn(book.getIsbn());
    existingBook.setPublicationDate(book.getPublicationDate());
    existingBook.setPages(book.getPages());
    existingBook.setGenre(book.getGenre());
    existingBook.setAuthor(book.getAuthor());

    return bookRepository.save(existingBook);
  }

  @Override
  @Transactional(readOnly = false)
  public void deleteById(Long id) {
    Book book = findById(id);
    bookRepository.delete(book);
  }

  @Override
  @Transactional(readOnly = false)
  public Book borrowBook(String isbn) {
    Book book = findByIsbn(isbn);

    if (!book.isAvailable()) {
      throw new BookAlreadyBorrowedException(isbn);
    }

    book.borrow();
    return bookRepository.save(book);
  }

  @Override
  @Transactional(readOnly = false)
  public Book returnBook(String isbn) {
    Book book = findByIsbn(isbn);
    book.returnBook();
    return bookRepository.save(book);
  }

  @Override
  public long countByAuthorAndStatus(Long authorId, BookStatus status) {
    return bookRepository.countByAuthorIdAndStatus(authorId, status);
  }

  @Override
  public boolean existsByIsbn(String isbn) {
    return bookRepository.existsByIsbn(isbn);
  }

  private void validateBook(Book book) {
    if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
      throw new IllegalArgumentException("Le titre du livre ne peut pas être vide");
    }

    if (book.getIsbn() == null || !isValidIsbn(book.getIsbn())) {
      throw new InvalidIsbnException(book.getIsbn());
    }

    if (existsByIsbn(book.getIsbn())) {
      throw new IllegalArgumentException("Un livre avec cet ISBN existe déjà");
    }

    if (book.getAuthor() == null) {
      throw new IllegalArgumentException("L'auteur ne peut pas être null");
    }
  }

  private boolean isValidIsbn(String isbn) {
    // Validation simplifiée pour l'exemple
    return isbn != null && (isbn.length() == 10 || isbn.length() == 13);
  }
}
