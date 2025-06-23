package com.formation.library.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorTest {

  private Author author;

  @BeforeEach
  void setup() {
    author = new Author("John Doe", "john@example.com");
  }

  @Test
  void shouldCreateAuthorWithNameAndEmail() {
    assertThat(author.getName()).isEqualTo("John Doe");
    assertThat(author.getEmail()).isEqualTo("john@example.com");
    assertThat(author.getBooks()).isEmpty();
  }

  @Test
  void shouldCreateAuthorWithAllFields() {
    LocalDate birthday = LocalDate.of(1970, 1, 25);
    Author fullAuthor = new Author("sam", "sam@sam.fr", birthday, "je suis un formateur");

    assertThat(fullAuthor.getName()).isEqualTo("sam");
    assertThat(fullAuthor.getEmail()).isEqualTo("sam@sam.fr");
    assertThat(fullAuthor.getBirthDate()).isEqualTo(birthday);
    assertThat(fullAuthor.getBiography()).isEqualTo("je suis un formateur");
  }

  @Test
  void shouldAddBookToAuthor() {
    Book book = new Book("Test Book", "1234567890", author);

    author.addBook(book);

    assertThat(author.getBooks()).hasSize(1);
    assertThat(author.getBooks()).contains(book);
    assertThat(book.getAuthor()).isEqualTo(author);
  }

  @Test
  void shouldRemoveBookFromAuthor() {
    Book book = new Book("Test Book", "1234567890", author);

    author.addBook(book);

    author.removeBook(book);

    assertThat(author.getBooks()).isEmpty();
    assertThat(book.getAuthor()).isNull();
  }

  @Test
  void shouldTestEqualsAndHashCode() {
    Author author1 = new Author("John Doe", "john@example.fr");
    Author author2 = new Author("John Doe", "john@example.fr");
    Author author3 = new Author("Jane Doe", "jane@example.fr");

    author1.setId(1L);
    author2.setId(1L);
    author3.setId(2L);

    assertThat(author1).isEqualTo(author2);
    assertThat(author1).isNotEqualTo(author3);
    // assertThat(author1.hashCode()).isEqualTo(author2.hashCode());
    assertThat(author1).hasSameHashCodeAs(author2);
  }

  @Test
  void shouldTestEqualsWithNullAndDifferentObject() {
    author.setId(1L);
    Author sameAuthor = author;

    assertThat(author).isNotEqualTo(null);
    assertThat(author).isNotEqualTo("string");
    assertThat(author).isEqualTo(sameAuthor);

    // assertThat(author).isNotEqualTo(null).isNotEqualTo("string").isEqualTo(sameAuthor);
  }

  @Test
  void shouldTestEqualsWithNullId() {
    Author author1 = new Author("John Doe", "john@example.fr");
    Author author2 = new Author("John Doe", "john@example.fr");

    assertThat(author1).isNotEqualTo(author2);
  }

  @Test
  void shouldTestAllGettersAndSetters() {
    author.setId(1L);
    author.setName("sam");
    author.setEmail("sam@sam.fr");
    author.setBirthDate(LocalDate.of(2025, 1, 1));
    author.setBiography("getBiography");
    author.setBooks(List.of());

    assertThat(author.getId()).isEqualTo(1L);
    assertThat(author.getName()).isEqualTo("sam");
    assertThat(author.getEmail()).isEqualTo("sam@sam.fr");
    assertThat(author.getBirthDate()).isEqualTo(LocalDate.of(2025, 1, 1));
    assertThat(author.getBiography()).isEqualTo("getBiography");
    assertThat(author.getBooks()).isNotNull();
  }

}
