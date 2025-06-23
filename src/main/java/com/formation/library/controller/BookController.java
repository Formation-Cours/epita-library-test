package com.formation.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.library.dto.BookDTO;
import com.formation.library.dto.BookWithAuthorDTO;
import com.formation.library.entity.Book;
import com.formation.library.entity.Book.BookStatus;
import com.formation.library.mapper.DTOMapper;
import com.formation.library.service.IBookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

  private final IBookService bookService;
  private final DTOMapper dtoMapper;

  public BookController(IBookService bookService, DTOMapper dtoMapper) {
    this.bookService = bookService;
    this.dtoMapper = dtoMapper;
  }

  @PostMapping
  public ResponseEntity<BookWithAuthorDTO> createBook(@Valid @RequestBody Book book) {
    Book savedBook = bookService.save(book);
    BookWithAuthorDTO bookDTO = dtoMapper.toBookWithAuthorDTO(savedBook);
    return new ResponseEntity<>(bookDTO, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookWithAuthorDTO> getBook(@PathVariable Long id) {
    Book book = bookService.findById(id);
    BookWithAuthorDTO bookDTO = dtoMapper.toBookWithAuthorDTO(book);
    return ResponseEntity.ok(bookDTO);
  }

  @GetMapping
  public ResponseEntity<List<BookDTO>> getAllBooks() {
    List<Book> books = bookService.findAll();
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @GetMapping("/isbn/{isbn}")
  public ResponseEntity<BookWithAuthorDTO> getBookByIsbn(@PathVariable String isbn) {
    Book book = bookService.findByIsbn(isbn);
    BookWithAuthorDTO bookDTO = dtoMapper.toBookWithAuthorDTO(book);
    return ResponseEntity.ok(bookDTO);
  }

  @GetMapping("/search")
  public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String title) {
    List<Book> books = bookService.findByTitle(title);
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<List<BookDTO>> getBooksByStatus(@PathVariable BookStatus status) {
    List<Book> books = bookService.findByStatus(status);
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @GetMapping("/genre/{genre}")
  public ResponseEntity<List<BookDTO>> getBooksByGenre(@PathVariable String genre) {
    List<Book> books = bookService.findByGenre(genre);
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @GetMapping("/author/{authorId}")
  public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long authorId) {
    List<Book> books = bookService.findByAuthorId(authorId);
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @GetMapping("/author/name")
  public ResponseEntity<List<BookDTO>> getBooksByAuthorName(@RequestParam String authorName) {
    List<Book> books = bookService.findByAuthorName(authorName);
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @GetMapping("/publication-date")
  public ResponseEntity<List<BookDTO>> getBooksByPublicationDate(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    List<Book> books = bookService.findByPublicationDateBetween(startDate, endDate);
    List<BookDTO> bookDTOs = dtoMapper.toBookDTOList(books);
    return ResponseEntity.ok(bookDTOs);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BookWithAuthorDTO> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
    Book updatedBook = bookService.update(id, book);
    BookWithAuthorDTO bookDTO = dtoMapper.toBookWithAuthorDTO(updatedBook);
    return ResponseEntity.ok(bookDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{isbn}/borrow")
  public ResponseEntity<BookWithAuthorDTO> borrowBook(@PathVariable String isbn) {
    Book borrowedBook = bookService.borrowBook(isbn);
    BookWithAuthorDTO bookDTO = dtoMapper.toBookWithAuthorDTO(borrowedBook);
    return ResponseEntity.ok(bookDTO);
  }

  @PatchMapping("/{isbn}/return")
  public ResponseEntity<BookWithAuthorDTO> returnBook(@PathVariable String isbn) {
    Book returnedBook = bookService.returnBook(isbn);
    BookWithAuthorDTO bookDTO = dtoMapper.toBookWithAuthorDTO(returnedBook);
    return ResponseEntity.ok(bookDTO);
  }
}
