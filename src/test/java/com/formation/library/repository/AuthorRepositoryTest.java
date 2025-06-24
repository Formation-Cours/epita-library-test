package com.formation.library.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.formation.library.entity.Author;

@DataJpaTest
@ActiveProfiles("test")
class AuthorRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AuthorRepository authorRepository;

  private Author author1;
  private Author author2;

  @BeforeEach
  void setUp() {
    author1 = new Author("Samuel Michaux", "sam@sam.fr", LocalDate.of(1981, 7, 9), "Je suis Formateur");
    author2 = new Author("Patrick Michaux", "pat@pat.fr", LocalDate.of(1959, 10, 26), "Je suis ton père");

    entityManager.persistAndFlush(author1);
    entityManager.persistAndFlush(author2);
  }

  @Test
  void shouldFindAuthorByEmail() {
    Optional<Author> author = authorRepository.findByEmail("sam@sam.fr");

    assertThat(author).isPresent();
    assertThat(author.get().getName()).isEqualTo("Samuel Michaux");
  }

  @Test
  void shouldFindAuthorByBirthDateBetween() {
    LocalDate start = LocalDate.of(1960, 1, 1);
    LocalDate end = LocalDate.of(1989, 12, 31);

    List<Author> authors = authorRepository.findByBirthDateBetween(start, end);

    assertThat(authors).hasSize(1);
    assertThat(authors.get(0).getEmail()).isEqualTo("sam@sam.fr");
  }
}
