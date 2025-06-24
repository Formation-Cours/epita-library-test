package com.formation.library.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class BookTest {

  private Author author;
  private Book book;

  @BeforeEach
  void setUp() {
    author = new Author("John Doe", "john@example.com");
    book = new Book("Test Book", "1234567890", author);
  }

  @Test
  void shouldCreateBookWithRequiredFields() {
    assertThat(book.getTitle()).isEqualTo("Test Book");
    assertThat(book.getIsbn()).isEqualTo("1234567890");
    assertThat(book.getAuthor()).isEqualTo(author);
    assertThat(book.getStatus()).isEqualTo(Book.BookStatus.AVAILABLE);
  }

  @Test
  void shouldCreateBookWithAllFields() {
    LocalDate publicationDate = LocalDate.of(2023, 1, 1);
    Book fullBook = new Book("Full Book", "0987654321", publicationDate, 300, "Fiction", author);

    assertThat(fullBook.getTitle()).isEqualTo("Full Book");
    assertThat(fullBook.getIsbn()).isEqualTo("0987654321");
    assertThat(fullBook.getPublicationDate()).isEqualTo(publicationDate);
    assertThat(fullBook.getPages()).isEqualTo(300);
    assertThat(fullBook.getGenre()).isEqualTo("Fiction");
    assertThat(fullBook.getAuthor()).isEqualTo(author);
  }

  @Test
  void shouldBeAvailableByDefault() {
    assertThat(book.isAvailable()).isTrue();
    assertThat(book.getStatus()).isEqualTo(Book.BookStatus.AVAILABLE);
  }

  @Test
  void shouldBorrowAvailableBook() {
    book.borrow();

    assertThat(book.getStatus()).isEqualTo(Book.BookStatus.BORROWED);
    assertThat(book.isAvailable()).isFalse();
  }

  @Test
  void shouldNotBorrowUnavailableBook() {
    book.setStatus(Book.BookStatus.BORROWED);

    assertThatThrownBy(() -> book.borrow())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Le livre n'est pas disponible pour l'emprunt");
  }

  @Test
  void shouldReturnBorrowedBook() {
    book.borrow();

    book.returnBook();

    assertThat(book.getStatus()).isEqualTo(Book.BookStatus.AVAILABLE);
    assertThat(book.isAvailable()).isTrue();
  }

  @Test
  void shouldNotReturnNonBorrowedBook() {
    assertThatThrownBy(() -> book.returnBook())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Le livre n'est pas emprunté");
  }

  @Test
  void shouldNotReturnReservedBook() {
    book.setStatus(Book.BookStatus.RESERVED);

    assertThatThrownBy(() -> book.returnBook())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Le livre n'est pas emprunté");
  }

  @Test
  void shouldTestAllBookStatuses() {
    book.setStatus(Book.BookStatus.AVAILABLE);
    assertThat(book.isAvailable()).isTrue();

    book.setStatus(Book.BookStatus.BORROWED);
    assertThat(book.isAvailable()).isFalse();

    book.setStatus(Book.BookStatus.RESERVED);
    assertThat(book.isAvailable()).isFalse();

    book.setStatus(Book.BookStatus.MAINTENANCE);
    assertThat(book.isAvailable()).isFalse();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    Book book1 = new Book("Title", "1111111111", author);
    Book book2 = new Book("Title", "1111111111", author);
    Book book3 = new Book("Other", "2222222222", author);

    book1.setId(1L);
    book2.setId(1L);
    book3.setId(2L);

    assertThat(book1)
        .isEqualTo(book2)
        .isNotEqualTo(book3)
        .hasSameHashCodeAs(book2)
        .isNotEqualTo(null);

    book1.setId(null);
    book2.setId(null);
    assertThat(book1).isNotEqualTo(book2);
  }

  @Test
  void shouldTestAllSettersAndGetters() {
    book.setId(1L);
    book.setTitle("Updated Title");
    book.setIsbn("0000000000");
    book.setPublicationDate(LocalDate.of(2023, 12, 25));
    book.setPages(500);
    book.setGenre("Science Fiction");
    book.setStatus(Book.BookStatus.RESERVED);

    assertThat(book.getId()).isEqualTo(1L);
    assertThat(book.getTitle()).isEqualTo("Updated Title");
    assertThat(book.getIsbn()).isEqualTo("0000000000");
    assertThat(book.getPublicationDate()).isEqualTo(LocalDate.of(2023, 12, 25));
    assertThat(book.getPages()).isEqualTo(500);
    assertThat(book.getGenre()).isEqualTo("Science Fiction");
    assertThat(book.getStatus()).isEqualTo(Book.BookStatus.RESERVED);
  }
}
