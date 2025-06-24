package com.formation.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.formation.library.entity.Author;
import com.formation.library.entity.Book;
import com.formation.library.entity.Book.BookStatus;
import com.formation.library.exception.BookAlreadyBorrowedException;
import com.formation.library.exception.BookNotFoundException;
import com.formation.library.exception.InvalidIsbnException;
import com.formation.library.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookServiceImpl bookService;

  private Author author;
  private Book book;

  @BeforeEach
  void setUp() {
    author = new Author("John Doe", "john@example.com");
    author.setId(1L);
    book = new Book("Test Book", "1234567890", author);
    book.setId(1L);
  }

  @Test
  void shouldSaveValidBook() {
    when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
    when(bookRepository.save(book)).thenReturn(book);

    Book savedBook = bookService.save(book);

    assertThat(savedBook).isEqualTo(book);
    verify(bookRepository).save(book);
  }

  @Test
  void shouldThrowExceptionWhenSavingBookWithNullTitle() {
    book.setTitle(null);

    assertThatThrownBy(() -> bookService.save(book))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le titre du livre ne peut pas être vide");
  }

  @Test
  void shouldThrowExceptionWhenSavingBookWithEmptyTitle() {
    book.setTitle("   ");

    assertThatThrownBy(() -> bookService.save(book))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Le titre du livre ne peut pas être vide");
  }

  @Test
  void shouldThrowExceptionWhenSavingBookWithNullIsbn() {
    book.setIsbn(null);

    assertThatThrownBy(() -> bookService.save(book))
        .isInstanceOf(InvalidIsbnException.class)
        .hasMessage("ISBN invalide: null");
  }

  @Test
  void shouldThrowExceptionWhenSavingBookWithInvalidIsbn() {
    book.setIsbn("123"); // Trop court

    assertThatThrownBy(() -> bookService.save(book))
        .isInstanceOf(InvalidIsbnException.class)
        .hasMessage("ISBN invalide: 123");
  }

  @Test
  void shouldThrowExceptionWhenSavingBookWithExistingIsbn() {
    when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);

    assertThatThrownBy(() -> bookService.save(book))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Un livre avec cet ISBN existe déjà");
  }

  @Test
  void shouldThrowExceptionWhenSavingBookWithNullAuthor() {
    book.setAuthor(null);

    assertThatThrownBy(() -> bookService.save(book))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("L'auteur ne peut pas être null");
  }

  @Test
  void shouldAcceptValidIsbn10() {
    book.setIsbn("1234567890");
    when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
    when(bookRepository.save(book)).thenReturn(book);

    Book savedBook = bookService.save(book);

    assertThat(savedBook).isNotNull();
  }

  @Test
  void shouldAcceptValidIsbn13() {
    book.setIsbn("1234567890123");
    when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
    when(bookRepository.save(book)).thenReturn(book);

    Book savedBook = bookService.save(book);

    assertThat(savedBook).isNotNull();
  }

  @Test
  void shouldFindBookById() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

    Book foundBook = bookService.findById(1L);

    assertThat(foundBook).isEqualTo(book);
    verify(bookRepository).findById(1L);
  }

  @Test
  void shouldThrowExceptionWhenBookNotFoundById() {
    when(bookRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> bookService.findById(1L))
        .isInstanceOf(BookNotFoundException.class)
        .hasMessage("Livre non trouvé avec l'ID: 1");
  }

  @Test
  void shouldFindBookByIsbn() {
    when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

    Book foundBook = bookService.findByIsbn("1234567890");

    assertThat(foundBook).isEqualTo(book);
    verify(bookRepository).findByIsbn("1234567890");
  }

  @Test
  void shouldThrowExceptionWhenBookNotFoundByIsbn() {
    when(bookRepository.findByIsbn("0000000000")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> bookService.findByIsbn("0000000000"))
        .isInstanceOf(BookNotFoundException.class)
        .hasMessage("Livre non trouvé avec l'ISBN: 0000000000");
  }

  @Test
  void shouldFindAllBooks() {
    List<Book> books = Arrays.asList(book, new Book("Another Book", "0987654321", author));
    when(bookRepository.findAll()).thenReturn(books);

    List<Book> foundBooks = bookService.findAll();

    assertThat(foundBooks).hasSize(2).containsExactlyElementsOf(books);
  }

  @Test
  void shouldFindBooksByTitle() {
    List<Book> books = Arrays.asList(book);
    when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(books);

    List<Book> foundBooks = bookService.findByTitle("Test");

    assertThat(foundBooks).hasSize(1).contains(book);
  }

  @Test
  void shouldFindBooksByStatus() {
    List<Book> books = Arrays.asList(book);
    when(bookRepository.findByStatus(BookStatus.AVAILABLE)).thenReturn(books);

    List<Book> foundBooks = bookService.findByStatus(BookStatus.AVAILABLE);

    assertThat(foundBooks).hasSize(1);
    verify(bookRepository).findByStatus(BookStatus.AVAILABLE);
  }

  @Test
  void shouldFindBooksByGenre() {
    List<Book> books = Arrays.asList(book);
    when(bookRepository.findByGenreIgnoreCase("Fiction")).thenReturn(books);

    List<Book> foundBooks = bookService.findByGenre("Fiction");

    assertThat(foundBooks).hasSize(1);
    verify(bookRepository).findByGenreIgnoreCase("Fiction");
  }

  @Test
  void shouldFindBooksByAuthorId() {
    List<Book> books = Arrays.asList(book);
    when(bookRepository.findByAuthorId(1L)).thenReturn(books);

    List<Book> foundBooks = bookService.findByAuthorId(1L);

    assertThat(foundBooks).hasSize(1);
    verify(bookRepository).findByAuthorId(1L);
  }

  @Test
  void shouldFindBooksByAuthorName() {
    List<Book> books = Arrays.asList(book);
    when(bookRepository.findByAuthorNameContaining("John")).thenReturn(books);

    List<Book> foundBooks = bookService.findByAuthorName("John");

    assertThat(foundBooks).hasSize(1);
    verify(bookRepository).findByAuthorNameContaining("John");
  }

  @Test
  void shouldFindBooksByPublicationDateBetween() {
    LocalDate start = LocalDate.of(2020, 1, 1);
    LocalDate end = LocalDate.of(2023, 12, 31);
    List<Book> books = Arrays.asList(book);
    when(bookRepository.findByPublicationDateBetween(start, end)).thenReturn(books);

    List<Book> foundBooks = bookService.findByPublicationDateBetween(start, end);

    assertThat(foundBooks).hasSize(1);
    verify(bookRepository).findByPublicationDateBetween(start, end);
  }

  @Test
  void shouldUpdateBook() {
    Book updateData = new Book("Updated Title", "0987654321", author);
    updateData.setPublicationDate(LocalDate.of(2023, 1, 1));
    updateData.setPages(400);
    updateData.setGenre("Science Fiction");

    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
    when(bookRepository.save(book)).thenReturn(book);

    Book updatedBook = bookService.update(1L, updateData);

    assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
    assertThat(updatedBook.getIsbn()).isEqualTo("0987654321");
    assertThat(updatedBook.getPublicationDate()).isEqualTo(LocalDate.of(2023, 1, 1));
    assertThat(updatedBook.getPages()).isEqualTo(400);
    assertThat(updatedBook.getGenre()).isEqualTo("Science Fiction");
    verify(bookRepository).save(book);
  }

  @Test
  void shouldDeleteBook() {
    when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

    bookService.deleteById(1L);

    verify(bookRepository).delete(book);
  }

  @Test
  void shouldBorrowAvailableBook() {
    when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
    when(bookRepository.save(book)).thenReturn(book);

    Book borrowedBook = bookService.borrowBook("1234567890");

    assertThat(borrowedBook.getStatus()).isEqualTo(BookStatus.BORROWED);
    verify(bookRepository).save(book);
  }

  @Test
  void shouldThrowExceptionWhenBorrowingUnavailableBook() {
    book.setStatus(BookStatus.BORROWED);
    when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));

    assertThatThrownBy(() -> bookService.borrowBook("1234567890"))
        .isInstanceOf(BookAlreadyBorrowedException.class)
        .hasMessage("Le livre avec l'ISBN 1234567890 est déjà emprunté");
  }

  @Test
  void shouldReturnBorrowedBook() {
    book.setStatus(BookStatus.BORROWED);
    when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(book));
    when(bookRepository.save(book)).thenReturn(book);

    Book returnedBook = bookService.returnBook("1234567890");

    assertThat(returnedBook.getStatus()).isEqualTo(BookStatus.AVAILABLE);
    verify(bookRepository).save(book);
  }

  @Test
  void shouldCountByAuthorAndStatus() {
    when(bookRepository.countByAuthorIdAndStatus(1L, BookStatus.AVAILABLE)).thenReturn(5L);

    long count = bookService.countByAuthorAndStatus(1L, BookStatus.AVAILABLE);

    assertThat(count).isEqualTo(5L);
    verify(bookRepository).countByAuthorIdAndStatus(1L, BookStatus.AVAILABLE);
  }

  @Test
  void shouldCheckIfBookExistsByIsbn() {
    when(bookRepository.existsByIsbn("1234567890")).thenReturn(true);

    boolean exists = bookService.existsByIsbn("1234567890");

    assertThat(exists).isTrue();
    verify(bookRepository).existsByIsbn("1234567890");
  }

}
